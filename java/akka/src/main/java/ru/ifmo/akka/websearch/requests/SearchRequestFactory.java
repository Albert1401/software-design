package ru.ifmo.akka.websearch.requests;

public interface SearchRequestFactory{
    SearchRequest forQuery(String query);
}
