package top.dzurl.chrome.capture.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dzurl.chrome.capture.core.model.PDFTaskModel;
import top.dzurl.chrome.capture.core.service.PDFService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PDFController {

    @Autowired
    private PDFService pdfService;


    /**
     * 捕获浏览器
     *
     * @param model
     * @return
     */
    @RequestMapping("pdf")
    public void pdf(HttpServletRequest request, HttpServletResponse response, PDFTaskModel model) {
        pdfService.task(request, response, model);
    }


    /**
     * json接收参数
     *
     * @param model
     * @return
     */
    @RequestMapping("pdfJson.json")
    public void pdfJson(HttpServletRequest request, HttpServletResponse response, @RequestBody PDFTaskModel model) {
        pdfService.task(request, response, model);
    }


}
