package wang.jpy.chrome.core.model;

import lombok.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import wang.jpy.chrome.core.util.NetUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Base64;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParameterBody {

    private String url;

    //base64编码
    private String body;

    //上传文件
    MultipartFile file;


    /**
     * 获取输入流
     *
     * @return
     */
    @SneakyThrows
    public InputStream getInputStream() {
        if (url == null && body == null && file == null) {
            throw new RuntimeException("url、body、file 参数至少需要一个参数: body为base64编码,file 为 formdata 文件上传 ");
        }
        if (StringUtils.hasText(url)) {
            final HttpResponse<InputStream> response = NetUtil.get(url);
            if (response.statusCode() != 200) {
                return null;
            }
            return response.body();
        } else if (file != null) {
            return file.getInputStream();
        } else if (StringUtils.hasText(body)) {
            return new ByteArrayInputStream(Base64.getDecoder().decode(body));
        }
        return null;
    }


}
