package wang.jpy.chrome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan("wang.jpy.chrome.core")
public class ChromeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChromeApplication.class, args);
	}

}
