package ru.ifmo.akka.websearch;

import akka.actor.UntypedActor;
import ru.ifmo.akka.websearch.requests.SearchRequest;
import ru.ifmo.akka.websearch.requests.SearchResponse;

import java.util.Collections;
import java.util.List;


public class Child extends UntypedActor {
    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof SearchRequest) {
            try {
                List<SearchResponse> response = ((SearchRequest) o).execute();
                if (response.size() != 0) {
                    getSender().tell(response.subList(0, Math.min(5, response.size())), getSelf());
                    return;
                }
            } catch (Exception e) {
            }
            getSender().tell(Collections.emptyList(), getSelf());
        }
    }
}