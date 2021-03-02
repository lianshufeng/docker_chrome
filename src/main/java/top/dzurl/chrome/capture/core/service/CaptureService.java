package top.dzurl.chrome.capture.core.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import top.dzurl.chrome.capture.core.model.CaptureTaskModel;
import top.dzurl.chrome.capture.core.util.response.ResponseUtil;

import java.io.File;

@Slf4j
@Service
public class CaptureService extends SuperService<CaptureTaskModel> {


    @Override
    public void task(ChromeDriver driver, CaptureTaskModel model, File outFile) {
        //完整截图: 需要重置dom元素的宽度与高度
        if (model.isFull()) {
            int width = Integer.parseInt(String.valueOf(driver.executeScript("return document.documentElement.scrollWidth")));
            int height = Integer.parseInt(String.valueOf(driver.executeScript("return document.documentElement.scrollHeight")));
            driver.manage().window().setSize(new Dimension(width, height));
        }

        File file = driver.getScreenshotAs(OutputType.FILE);
        file.renameTo(outFile);
    }

    @Override
    public ResponseUtil.MimeType mimeType() {
        return new ResponseUtil.MimeType("png", "image/png");
    }


}
