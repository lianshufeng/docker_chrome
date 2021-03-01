package top.dzurl.chrome.capture.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dzurl.chrome.capture.core.model.CaptureTaskModel;
import top.dzurl.chrome.capture.core.model.TaskModel;
import top.dzurl.chrome.capture.core.service.CaptureService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class CaptureController {

    @Autowired
    private CaptureService captureService;

    /**
     * 捕获浏览器
     *
     * @param model
     * @return
     */
    @RequestMapping("capture")
    public void capture(HttpServletRequest request, HttpServletResponse response, CaptureTaskModel model) {
        captureService.task(request, response, model);
    }

    /**
     * json接收参数
     *
     * @param model
     * @return
     */
    @RequestMapping("capture.json")
    public void captureJson(HttpServletRequest request, HttpServletResponse response, @RequestBody CaptureTaskModel model) {
        captureService.task(request, response, model);
    }


}
