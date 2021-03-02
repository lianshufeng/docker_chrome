package top.dzurl.chrome.capture.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public abstract class TaskModel {

    //请求的地址
    private String url;

    //打开页面后等待时间
    private Long wait;

    //宽度
    private int width = 1024;

    //高度
    private int height = 768;

}
