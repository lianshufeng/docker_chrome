package top.dzurl.chrome.capture.core.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dzurl.chrome.capture.core.model.CaptureTaskModel;
import top.dzurl.chrome.capture.core.model.PDFTaskModel;

import java.util.HashMap;
import java.util.Map;

@RestController
public class IndexController {
    @RequestMapping({"/", ""})
    public Object index() {
        return parm();
    }

    /**
     * 构建默认参数
     *
     * @return
     */
    private static final Map<String, Object> parm() {
        return new HashMap<String, Object>() {{
            put("time", System.currentTimeMillis());
            put("task", new Model[]{
                    Model.builder().build().setUri(new String[]{"capture", "capture.json"}).setParameter(new CaptureTaskModel()),
                    Model.builder().build().setUri(new String[]{"pdf", "pdf.json"}).setParameter(new PDFTaskModel())
            });


//            put("parameter", new HashMap<String, Object>() {{
//                put("uri", new String[]{"capture", "capture.json"});
//                put("url", "网页地址");
//                put("width", "网页宽度");
//                put("height", "网页高度");
//            }});
        }};
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    private static class Model {

        //接口名
        private String[] uri;

        //接口参数
        private Object parameter;


    }

}
