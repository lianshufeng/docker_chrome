package top.dzurl.chrome.capture.core.service;

import lombok.Cleanup;
import lombok.SneakyThrows;
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

public abstract class SuperService<T extends TaskModel> {

    /**
     * 默认的命令行
     *
     * @return
     */
    public abstract String command();


    /**
     * 文件类型
     *
     * @return
     */
    public abstract ResponseUtil.MimeType mimeType();


    /**
     * 执行任务
     *
     * @param request
     * @param response
     * @param model
     */
    @SneakyThrows
    public void task(HttpServletRequest request, HttpServletResponse response, T model) {

        //构建临时文件
        @Cleanup("delete") File file = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + ".png");
        model.setOutput(file.getAbsolutePath());

        //执行命令行
        String cmd = buildCmd(model);

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
        ResponseUtil.writeStream(request, response, inputStream, sourceSize, mimeType());
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
    private String buildCmd(TaskModel model) {
        return SpringELUtil.parseExpression(model, command()).toString();
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
