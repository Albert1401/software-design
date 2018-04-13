package ru.ifmo.akka.websearch.requests.stub;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import ru.ifmo.akka.websearch.requests.SearchResponse;

public class SearchResponseImpl implements SearchResponse {
    @JsonProperty
    String url;

    @JsonIgnore
    String name;

    public SearchResponseImpl() { }

    public SearchResponseImpl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getName() {
        return name;
    }
}
