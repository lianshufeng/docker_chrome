package wang.jpy.chrome.core.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


@Slf4j
public class NetUtil {


    private final static long TimeOutSeconds = 60 * 60 * 4;


    @SneakyThrows
    public static HttpResponse<InputStream> get(String url) {
        log.info("request : {} ", url);
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TimeOutSeconds))
                .followRedirects(HttpClient.Redirect.NEVER).build();

        HttpRequest.Builder httpBuilder = HttpRequest.newBuilder();

        //通信时间超时
        httpBuilder.timeout(Duration.ofSeconds(TimeOutSeconds));

        //url
        httpBuilder.uri(new URI(url));

        //
        return httpClient.send(httpBuilder.GET().build(), HttpResponse.BodyHandlers.ofInputStream());
    }

}
