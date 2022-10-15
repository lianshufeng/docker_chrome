package wang.jpy.chrome.core.controller;

import groovy.lang.GroovyShell;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.jpy.chrome.core.model.ParameterBody;
import wang.jpy.chrome.core.util.GroovyUtil;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("script")
public class ScriptController {


    @SneakyThrows
    @RequestMapping("run")
    public Object run(String host, ParameterBody parameterBody) {
        @Cleanup InputStream inputStream = parameterBody.getInputStream();
        String scriptText = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
        //转换为运行脚本
        @Cleanup("quit") WebDriver webDriver = createDriver(host);

        GroovyUtil.runScript(new HashMap<>() {{
            put("driver", webDriver);
        }}, scriptText);


        return null;
    }


    @SneakyThrows
    private WebDriver createDriver(String host) {
        ChromiumOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("–incognito", "–disable-infobars");
//        chromeOptions.addArguments("–disable-infobars");
//        chromeOptions.addArguments("–headless");
//        chromeOptions.setCapability("browserVersion", "100");
//        chromeOptions.setCapability("platformName", "Windows");
//        chromeOptions.setCapability("se:name", "My simple test");
//        chromeOptions.setCapability("se:sampleMetadata", "Sample metadata value");
        WebDriver driver = new RemoteWebDriver(new URL(host), chromeOptions);
        return driver;
    }

}
