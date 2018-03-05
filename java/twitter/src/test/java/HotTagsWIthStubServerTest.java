import com.xebialabs.restito.server.StubServer;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import sd.ifmo.ru.twitter.api.TwitterApi;
import sd.ifmo.ru.twitter.api.TwitterApiImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.function.Consumer;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.method;

public class HotTagsWIthStubServerTest {
    private static final int PORT = 32453;

    @Test
    public void testRandom() {
        int[] freqs = new int[24];
        Random random = new Random();
        for (int i = 0; i < 24; i++) {
            freqs[i] = random.nextInt(50);
        }
        TweetGenerator generator = new TweetGenerator(freqs, true);
        withStubServer(PORT, s -> {
            whenHttp(s).match(method(Method.GET))
                    .then(x -> {
                        String str = x.getRequest().getRequestURL()
                                .append("?")
                                .append(x.getRequest().getQueryString())
                                .toString();
                        System.out.println(str);
                        return stringContent(generator.generateAnswer(str)).apply(x);
                    });
            StubHttpClient httpClient = new StubHttpClient("localhost:" + PORT);
            TwitterApi twitterApi = new TwitterApiImpl("a", "b", "c", "d");
            try {
                Field clientField = twitterApi.getClass().getDeclaredField("client");
                clientField.setAccessible(true);
                clientField.set(twitterApi, httpClient);
                HotTags tags = new HotTags(twitterApi);
                int[] ffreqs = tags.getFrequencies("testtag");
                Assert.assertArrayEquals(freqs, ffreqs);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testEquals() {
        TwitterApi api = null;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("keys.txt"))) {
            api = new TwitterApiImpl(
                    reader.readLine().trim(),
                    reader.readLine().trim(),
                    reader.readLine().trim(),
                    reader.readLine().trim()
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        HotTags tags = new HotTags(api);
        int[] freq1 = tags.getFrequencies("mandarin");
        int[] freq2 = tags.getFrequencies("mandarin");
        Assert.assertArrayEquals(freq1, freq2);
    }

    @Test(expected = java.io.UncheckedIOException.class)
    public void testServerError() {
        withStubServer(PORT, s -> {
            whenHttp(s).match(method(Method.GET))
                    .then(status(HttpStatus.BAD_REQUEST_400));
            StubHttpClient httpClient = new StubHttpClient("localhost:" + PORT);
            TwitterApi twitterApi = new TwitterApiImpl("a", "b", "c", "d");
            try {
                Field clientField = twitterApi.getClass().getDeclaredField("client");
                clientField.setAccessible(true);
                clientField.set(twitterApi, httpClient);
                HotTags tags = new HotTags(twitterApi);
                int[] ffreqs = tags.getFrequencies("testtag");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void withStubServer(int port, Consumer<StubServer> callback) {
        StubServer stubServer = null;
        try {
            stubServer = new StubServer(port).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }
}