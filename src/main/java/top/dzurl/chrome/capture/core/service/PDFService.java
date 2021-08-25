package top.dzurl.chrome.capture.core.service;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import top.dzurl.chrome.capture.core.model.PDFTaskModel;
import top.dzurl.chrome.capture.core.util.response.ResponseUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class PDFService extends SuperService<PDFTaskModel> {


    @SneakyThrows
    @Override
    public void task(ChromeDriver driver, PDFTaskModel model, File outFile) {
        if (model.isFull()) {
            int width = Integer.parseInt(String.valueOf(driver.executeScript("return document.documentElement.scrollWidth")));
            int height = Integer.parseInt(String.valueOf(driver.executeScript("return document.documentElement.scrollHeight")));
            driver.manage().window().setSize(new Dimension(width, height));
        }
        Map<String,Object> res = driver.executeCdpCommand("Page.printToPDF", new HashMap<>());
        FileUtils.writeByteArrayToFile(outFile, Base64.getDecoder().decode(String.valueOf(res.get("data"))));
    }

    @Override
    public ResponseUtil.MimeType mimeType() {
        return new ResponseUtil.MimeType("pdf", "application/pdf");
    }


}
