package top.dzurl.chrome.capture.core.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.dzurl.chrome.capture.core.model.PDFTaskModel;
import top.dzurl.chrome.capture.core.util.response.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class PDFService extends SuperService<PDFTaskModel> {

//    private final static String Command = "chrome --disable-dev-shm-usage --no-sandbox --headless --disable-gpu --window-size=#width,#height --print-to-pdf=#output --process-per-site #url";


    @Override
    public String command() {
        return "'chrome --disable-dev-shm-usage --no-sandbox --headless --disable-gpu --print-to-pdf='+#output+' --process-per-site ' + #url";
    }

    @Override
    public ResponseUtil.MimeType mimeType() {
        return new ResponseUtil.MimeType("pdf", "application/pdf");
    }

    @Override
    public void task(HttpServletRequest request, HttpServletResponse response, PDFTaskModel model) {
        Assert.hasText(model.getUrl(), "URL地址不能为空");
        super.task(request, response, model);
    }
}
