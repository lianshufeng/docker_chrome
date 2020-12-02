package top.dzurl.chrome.capture.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dzurl.chrome.capture.core.model.TaskModel;
import top.dzurl.chrome.capture.core.service.CaptureService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class IndexController {


    @Autowired
    private CaptureService captureService;


    @RequestMapping({"/", ""})
    public Object index() {
        return parm();
    }


    /**
     * 捕获浏览器
     *
     * @param model
     * @return
     */
    @RequestMapping("capture")
    public void capture(HttpServletRequest request, HttpServletResponse response, TaskModel model) {
        this.executeCapture(request, response, model);
    }

    /**
     * json接收参数
     *
     * @param model
     * @return
     */
    @RequestMapping("capture.json")
    public void captureJson(HttpServletRequest request, HttpServletResponse response, @RequestBody TaskModel model) {
        this.executeCapture(request, response, model);
    }

    /**
     * 执行截图程序
     *
     * @param response
     * @param model
     */
    private void executeCapture(HttpServletRequest request, HttpServletResponse response, @RequestBody TaskModel model) {
        validaModel(model);
        captureService.capture(request, response, model);
    }


    /**
     * 构建默认参数
     *
     * @return
     */
    private static final Map<String, Object> parm() {
        return new HashMap<String, Object>() {{
            put("time", System.currentTimeMillis());
            put("uri", new String[]{"capture", "capture.json"});
            put("parameter", new HashMap<String, Object>() {{
                put("url", "网页地址");
                put("width", "网页宽度");
                put("height", "网页高度");
            }});
        }};

    }

    private void validaModel(TaskModel model) {
        Assert.hasText(model.getUrl(), "URL不能为空");
    }


}
