import sd.ifmo.ru.twitter.api.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;

public class StubHttpClient implements HttpClient {
    private String proxyHost;

    public StubHttpClient(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    @Override
    public String getBody(String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            URL surl = new URL(requestUrl.replace(url.getHost(), proxyHost).replace("https", "http"));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(surl.openConnection().getInputStream()))) {
                return reader.lines().reduce((x, y) -> x + "\n" + y).orElse("");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
