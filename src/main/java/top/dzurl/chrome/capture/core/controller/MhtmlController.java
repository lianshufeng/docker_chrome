package top.dzurl.chrome.capture.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dzurl.chrome.capture.core.model.MHtmlTaskModel;
import top.dzurl.chrome.capture.core.service.MHTmlService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class MhtmlController {

    @Autowired
    private MHTmlService service;


    /**
     * 捕获浏览器
     *
     * @param model
     * @return
     */
    @RequestMapping("html")
    public void html(HttpServletRequest request, HttpServletResponse response, MHtmlTaskModel model) {
        service.task(request, response, model);
    }


    /**
     * json接收参数
     *
     * @param model
     * @return
     */
    @RequestMapping("html.json")
    public void htmlJson(HttpServletRequest request, HttpServletResponse response, @RequestBody MHtmlTaskModel model) {
        service.task(request, response, model);
    }


}
