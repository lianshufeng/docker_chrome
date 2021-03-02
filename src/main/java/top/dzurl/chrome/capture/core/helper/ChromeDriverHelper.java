package top.dzurl.chrome.capture.core.helper;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.Config;
import lombok.SneakyThrows;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ChromeDriverHelper implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        install();
    }


    /**
     * 安装Chrome的驱动
     */
    @SneakyThrows
    public void install() {
        WebDriverManager webDriverManager = WebDriverManager.chromedriver();
        Config config = webDriverManager.config();
        //使用淘宝镜像安装
        config.setUseMirror(true);
        WebDriverManager.chromedriver().setup();
    }


}
