package ru.ifmo.akka.websearch;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Pair;
import akka.pattern.AskTimeoutException;
import akka.pattern.Patterns;
import ru.ifmo.akka.websearch.requests.SearchResponse;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import ru.ifmo.akka.websearch.requests.SearchRequestFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author akirakozov
 */

public class Aggregator extends UntypedActor {
    private List<SearchRequestFactory> requestGenerators;


    public Aggregator(List<SearchRequestFactory> requestGenerators) {
        this.requestGenerators = requestGenerators;
    }


    @Override
    public void onReceive(Object query) throws Throwable {
        if (query instanceof String) {

            List<Pair<ActorRef, Future<?>>> chilrenInfo = new ArrayList<>();
            for (SearchRequestFactory requestGenerator : requestGenerators) {
                ActorRef ref = getContext().actorOf(Props.create(Child.class));
                Future<?> ask = Patterns.ask(ref, requestGenerator.forQuery((String) query), 4 * 1000);
                chilrenInfo.add(new Pair<>(ref, ask));
            }


            List<SearchResponse> all = new ArrayList<>();
            for (Pair<ActorRef, Future<?>> info : chilrenInfo) {
                try {
                    List<SearchResponse> response = (List<SearchResponse>) Await.result(info.second(), new FiniteDuration(10, TimeUnit.SECONDS));
                    all.addAll(response);
                } catch (AskTimeoutException e) {

                } finally {
                    getContext().stop(info.first());
                }
            }
            getSender().tell(all, getSelf());
        }
    }

}
