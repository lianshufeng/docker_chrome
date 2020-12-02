package top.dzurl.chrome.capture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("top.dzurl.chrome.capture.core")
@SpringBootApplication
public class CaptureApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaptureApplication.class, args);
    }

}
