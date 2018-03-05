package sd.ifmo.ru.twitter.api;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class TwitterApiOAuthHttpClient implements HttpClient {
    private OAuth1AccessToken token;

    private OAuth10aService service;

    public TwitterApiOAuthHttpClient(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        Objects.requireNonNull(consumerKey);
        Objects.requireNonNull(consumerSecret);
        Objects.requireNonNull(accessToken);
        Objects.requireNonNull(accessTokenSecret);
        this.service = new ServiceBuilder(consumerKey)
                .apiSecret(consumerSecret)
                .build(com.github.scribejava.apis.TwitterApi.instance());

        this.token = new OAuth1AccessToken(accessToken, accessTokenSecret);
    }

    @Override
    public String getBody(String requestUrl) {
        OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
        service.signRequest(token, request);
        try {
            Response response = service.execute(request);
            if (response.getCode() == 200) {
                return response.getBody();
            } else {
                throw new UncheckedIOException(new IOException("bad response code: " + response.getCode()));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
