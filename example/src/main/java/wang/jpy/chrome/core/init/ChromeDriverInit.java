package wang.jpy.chrome.core.init;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ChromeDriverInit {


    @Autowired
    private void init(ApplicationContext applicationContext) {
        WebDriverManager webDriverManager = WebDriverManager.getInstance();
        Config config = webDriverManager.config();
        //使用淘宝镜像安装
        config.setUseMirror(true);
        WebDriverManager.chromedriver().setup();
    }


}
