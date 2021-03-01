package top.dzurl.chrome.capture.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.dzurl.chrome.capture.core.model.CaptureTaskModel;
import top.dzurl.chrome.capture.core.util.response.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class CaptureService extends SuperService<CaptureTaskModel> {

    @Override
    public String command() {
        return "'chrome --disable-dev-shm-usage --no-sandbox --headless --disable-gpu --window-size='+#width+','+#height+' --screenshot='+#output+' --process-per-site ' + #url";
    }


    @Override
    public ResponseUtil.MimeType mimeType() {
        return new ResponseUtil.MimeType("png", "image/png");
    }

    @Override
    public void task(HttpServletRequest request, HttpServletResponse response, CaptureTaskModel model) {
        Assert.hasText(model.getUrl(), "URL地址不能为空");
        super.task(request, response, model);
    }
}
