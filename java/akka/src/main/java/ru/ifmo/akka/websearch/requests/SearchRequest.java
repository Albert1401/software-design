package ru.ifmo.akka.websearch.requests;

import java.util.List;

public interface SearchRequest{
    List<SearchResponse> execute();
}
