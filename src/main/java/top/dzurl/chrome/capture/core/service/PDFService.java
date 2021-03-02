package top.dzurl.chrome.capture.core.service;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.dzurl.chrome.capture.core.model.PDFTaskModel;
import top.dzurl.chrome.capture.core.util.SpringELUtil;
import top.dzurl.chrome.capture.core.util.response.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

@Service
public class PDFService extends SuperService<PDFTaskModel> {

    private final static String Command = "'chrome --disable-dev-shm-usage --no-sandbox --headless --disable-gpu  --print-to-pdf='+#output+' --process-per-site '+#url";

    @Override
    @SneakyThrows
    public void task(HttpServletRequest request, HttpServletResponse response, PDFTaskModel model) {
        Assert.hasText(model.getUrl(), "URL地址不能为空");

        //构建临时文件
        @Cleanup("delete") File outFile = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + ".png");


        //执行命令行
        String cmd = SpringELUtil.parseExpression(new HashMap<String, Object>() {{
            put("url", model.getUrl());
            put("output", outFile.getAbsolutePath());
        }}, Command).toString();


        //生产命令文件
        @Cleanup("delete") File cmdFile = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + ".cmd");
        @Cleanup FileOutputStream fileOutputStream = new FileOutputStream(cmdFile);
        fileOutputStream.write(cmd.getBytes(StandardCharsets.UTF_8));

        //执行命令文件
        runCmdFile(cmdFile);

        //写出流
        if (outFile.exists()) {
            @Cleanup FileInputStream imageInputFileStream = new FileInputStream(outFile);
            writeStream(request, response, imageInputFileStream, outFile.length());
        } else {
            response.sendError(404);
        }
    }

    @Override
    public void task(ChromeDriver driver, PDFTaskModel model, File outFile) {
        //nothing...
    }

    public String[] options(PDFTaskModel model, File outFile) {
        return new String[]{
                "--incognito",
                "--allow-running-insecure-content",
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--headless",
                "--disable-gpu",
                "--print-to-pdf"
        };
    }


    @Override
    public ResponseUtil.MimeType mimeType() {
        return new ResponseUtil.MimeType("pdf", "application/pdf");
    }


}
