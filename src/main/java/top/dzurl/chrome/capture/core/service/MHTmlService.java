package top.dzurl.chrome.capture.core.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import top.dzurl.chrome.capture.core.model.MHtmlTaskModel;
import top.dzurl.chrome.capture.core.util.response.ResponseUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Slf4j
@Service
public class MHTmlService extends SuperService<MHtmlTaskModel> {


    @Override
    @SneakyThrows
    public void task(ChromeDriver driver, MHtmlTaskModel model, File outFile) {
        Object res = driver.executeCdpCommand("Page.captureSnapshot", new HashMap<>());
        FileUtils.writeByteArrayToFile(outFile, String.valueOf(res).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public ResponseUtil.MimeType mimeType() {
        return new ResponseUtil.MimeType("mhtml", "multipart/related");
    }


}
