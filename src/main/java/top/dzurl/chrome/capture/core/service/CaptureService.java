package top.dzurl.chrome.capture.core.service;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dzurl.chrome.capture.core.conf.ChromeCommandConf;
import top.dzurl.chrome.capture.core.model.CaptureRequestModel;
import top.dzurl.chrome.capture.core.model.TaskModel;
import top.dzurl.chrome.capture.core.util.SpringELUtil;
import top.dzurl.chrome.capture.core.util.response.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class CaptureService {

    @Autowired
    private ChromeCommandConf chromeCommandConf;

    /**
     * 调用浏览器截图
     *
     * @return
     */
    @SneakyThrows
    public void capture(HttpServletRequest request, HttpServletResponse response, TaskModel model) {
        CaptureRequestModel requestModel = new CaptureRequestModel();
        BeanUtils.copyProperties(model, requestModel);

        //构建临时文件
        @Cleanup("delete") File file = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + ".png");
        requestModel.setOutput(file.getAbsolutePath());

        //执行命令行
        String cmd = buildCmd(requestModel);

        //生产命令文件
        @Cleanup("delete") File cmdFile = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + ".cmd");
        @Cleanup FileOutputStream fileOutputStream = new FileOutputStream(cmdFile);
        fileOutputStream.write(cmd.getBytes());

        //执行命令文件
        runCmdFile(cmdFile);

        //写出流
        if (file.exists()) {
            @Cleanup FileInputStream imageInputFileStream = new FileInputStream(file);
            writeStream(request, response, imageInputFileStream, file.length(), "image/png");
        } else {
            response.sendError(404);
        }
    }

    /**
     * 写出流
     *
     * @param response
     * @param inputStream
     * @param type
     */
    @SneakyThrows
    private void writeStream(HttpServletRequest request, HttpServletResponse response, InputStream inputStream, long sourceSize, String type) {
        ResponseUtil.writeStream(request, response, inputStream, sourceSize, new ResponseUtil.MimeType("png", "image/png"));
    }

    /**
     * 启动命令行
     */
    @SneakyThrows
    private static void runCmdFile(File cmdFile) {
        Runtime runtime = Runtime.getRuntime();
        @Cleanup("destroy") Process p = runtime.exec(createStartCmd(cmdFile));
        p.waitFor();
    }


    /**
     * 构建命令行
     *
     * @return
     */
    private String buildCmd(CaptureRequestModel model) {
        Object cmd = SpringELUtil.parseExpression(model, this.chromeCommandConf.getCmd());
        return String.valueOf(cmd);
    }

    /**
     * 是否win系统
     *
     * @return
     */
    private static boolean isWin() {
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }


    /**
     * 构建启动的命令行
     *
     * @param cmdFile
     * @return
     */
    private static String[] createStartCmd(File cmdFile) {
        String filePath = cmdFile.getAbsolutePath();
        return isWin() ? new String[]{"cmd", "/c", filePath} : new String[]{"sh", filePath};
    }


}
