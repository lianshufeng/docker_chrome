package top.dzurl.chrome.capture.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskModel {

    /**
     * 请求的URL
     */
    private String url;

    /**
     * 宽度
     */
    private long width = 1024;

    /**
     * 高度
     */
    private long height = 768;

    /**
     * 等待页面加载时间
     */
    private long waitTime = 0;
}
