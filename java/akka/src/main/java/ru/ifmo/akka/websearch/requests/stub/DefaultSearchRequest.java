package ru.ifmo.akka.websearch.requests.stub;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.codehaus.jackson.map.ObjectMapper;
import ru.ifmo.akka.websearch.requests.SearchRequest;
import ru.ifmo.akka.websearch.requests.SearchResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultSearchRequest implements SearchRequest {
    protected String queryUrl;
    protected String name;

    public DefaultSearchRequest(String queryUrl, String name) {
        this.queryUrl = queryUrl;
        this.name = name;
    }

    @Override
    public List<SearchResponse> execute() {
        try {
            String arr = Unirest.get(queryUrl).asObject(String.class).getBody();
            ObjectMapper mapper = new ObjectMapper();
            return Arrays.stream(mapper.readValue(arr, SearchResponseImpl[].class))
                    .peek(s -> s.name = name)
                    .collect(Collectors.toList());
        } catch (IOException | UnirestException e) {
            throw new RuntimeException(e);
        }
    }
}
