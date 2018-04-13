package ru.ifmo.akka.websearch.requests.stub;

import com.sun.net.httpserver.HttpServer;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class Servers {

    public static class Configuration {
        public static String googleUrl = "http://127.0.0.1:8081/?q=%s";
        public static int googlePort = 8081;

        public static String bingUrl = "http://127.0.0.1:8082/?q=%s";
        public static int bingPort = 8082;

        public static String yahooUrl = "http://127.0.0.1:8083/?q=%s";
        public static int yahooPort = 8083;
    }

    private static HttpServer stubServer(int port, List<String> output, int delay) {
        HttpServer server = null;
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(port), 0);
            server.createContext("/", r -> {
                ObjectMapper mapper = new ObjectMapper();

                byte[] bresp = mapper.writeValueAsBytes(
                        output.stream()
                                .map(SearchResponseImpl::new)
                                .collect(Collectors.toList())
                );
                if (delay != 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                r.sendResponseHeaders(200, bresp.length);
                r.getResponseBody().write(bresp);
                r.close();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return server;
    }

    public static class StubServerConfig {
        private int port;
        private List<String> output;
        private int delay;

        public StubServerConfig(int port, List<String> output, int delay) {
            this.port = port;
            this.output = output;
            this.delay = delay;
        }
    }

    public static void withAllServers(Runnable runnable,
                                      List<StubServerConfig> configs) {
        List<HttpServer> servers = configs.stream()
                .map(c -> stubServer(c.port, c.output, c.delay))
                .collect(Collectors.toList());

        servers.forEach(HttpServer::start);

        runnable.run();

        servers.forEach(s -> s.stop(0));

    }
}
