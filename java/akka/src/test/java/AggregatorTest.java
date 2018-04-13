import org.junit.Assert;
import org.junit.Test;
import ru.ifmo.akka.websearch.Main;
import ru.ifmo.akka.websearch.requests.SearchResponse;
import ru.ifmo.akka.websearch.requests.stub.Servers;

import java.util.*;
import java.util.stream.Collectors;

public class AggregatorTest {
    private List<String> generate(int n){
        List<String> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            res.add(i + "");
        }
        return res;
    }

    private List<String> get(List<SearchResponse> r, String provider){
        return r.stream().filter(s -> s.getName().equals(provider)).map(SearchResponse::getUrl).sorted().collect(Collectors.toList());
    }

    @Test
    public void test1(){
        Main main = new Main();
        List<String> out0 = generate(0);
        List<String> out4 = generate(4);
        List<String> out10 = generate(10);

        List<Servers.StubServerConfig> ordinayConf = Arrays.asList(
                new Servers.StubServerConfig(Servers.Configuration.googlePort, out0, 0),
                new Servers.StubServerConfig(Servers.Configuration.bingPort, out4, 0),
                new Servers.StubServerConfig(Servers.Configuration.yahooPort, out10, 0)
        );
        Servers.withAllServers(() -> {
            List<SearchResponse> r = main.processQuery("s");
            main.terminate();
            Assert.assertEquals(out0, get(r, "google"));
            Assert.assertEquals(out4, get(r, "bing"));
            Assert.assertEquals(generate(5), get(r, "yahoo"));
        }, ordinayConf);
    }

    @Test
    public void test2(){
        Main main = new Main();

        List<Servers.StubServerConfig> ordinayConf = Arrays.asList(
                new Servers.StubServerConfig(Servers.Configuration.googlePort, generate(50), 40 * 1000),
                new Servers.StubServerConfig(Servers.Configuration.bingPort, generate(100), 40 * 1000),
                new Servers.StubServerConfig(Servers.Configuration.yahooPort, generate(100), 100)
        );
        Servers.withAllServers(() -> {
            List<SearchResponse> r = main.processQuery("s");
            main.terminate();
            Assert.assertEquals(Collections.emptyList(), get(r, "google"));
            Assert.assertEquals(Collections.emptyList(), get(r, "bing"));
            Assert.assertEquals(generate(5), get(r, "yahoo"));

        }, ordinayConf);
    }
}
