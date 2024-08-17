import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static CloseableHttpClient httpClient = HttpClientBuilder.create()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setSocketTimeout(30000)
                    .setRedirectsEnabled(false)
                    .build())
            .build();

    private static ObjectMapper mapper = new ObjectMapper();

    public static String getUrl(String uri) throws IOException {
        CloseableHttpResponse httpResponse = httpClient.execute(new HttpGet(uri));
        NasaObject object = mapper.readValue(httpResponse.getEntity().getContent(), NasaObject.class);
        return object.getUrl();
    }

    public static String getDesc(String uri) throws IOException {
        CloseableHttpResponse httpResponse = httpClient.execute(new HttpGet(uri));
        NasaObject object = mapper.readValue(httpResponse.getEntity().getContent(), NasaObject.class);
        return object.getExplanation();
    }
}
