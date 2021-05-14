package top.dzurl.chrome.capture.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "chrome")
public class ChromeCommandConf {

    private long timeout = 60 * 1000;

}
