package ru.ifmo.akka.websearch.requests.stub;

import ru.ifmo.akka.websearch.requests.SearchRequest;
import ru.ifmo.akka.websearch.requests.SearchRequestFactory;

public class Generators {

    public static final SearchRequestFactory googleFactory = new SearchRequestFactory() {
        @Override
        public SearchRequest forQuery(String query) {
            return new DefaultSearchRequest(String.format(Servers.Configuration.googleUrl, query), "google");
        }
    };

    public static final SearchRequestFactory bingFactory = new SearchRequestFactory() {
        @Override
        public SearchRequest forQuery(String query) {
            return new DefaultSearchRequest(String.format(Servers.Configuration.bingUrl, query), "bing");
        }
    };

    public static final SearchRequestFactory yahooFactory = new SearchRequestFactory() {
        @Override
        public SearchRequest forQuery(String query) {
            return new DefaultSearchRequest(String.format(Servers.Configuration.yahooUrl, query), "yahoo");
        }
    };


}
