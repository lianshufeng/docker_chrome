package top.dzurl.chrome.capture.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "chrome")
public class ChromeCommandConf {

    /**
     * chrome对应的命令行
     */
    private String cmd = "'chrome --disable-dev-shm-usage --no-sandbox --headless --disable-gpu --window-size='+#width+','+#height+' --screenshot='+#output+' --process-per-site ' + #url";


}
