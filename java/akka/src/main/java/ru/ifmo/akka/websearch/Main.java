package ru.ifmo.akka.websearch;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Creator;
import akka.pattern.Patterns;
import akka.util.Timeout;
import ru.ifmo.akka.websearch.requests.SearchRequestFactory;
import ru.ifmo.akka.websearch.requests.SearchResponse;
import ru.ifmo.akka.websearch.requests.stub.Generators;
import ru.ifmo.akka.websearch.requests.stub.Servers;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    List<SearchRequestFactory> services;
    ActorSystem system;

    public Main() {
        this(Arrays.asList(Generators.bingFactory, Generators.googleFactory, Generators.yahooFactory),
                ActorSystem.create("MyAggregatorSystem"));
    }

    public Main(List<SearchRequestFactory> services, ActorSystem system) {
        this.services = services;
        this.system = system;
    }

    public List<SearchResponse> processQuery(String query) {
        ActorRef actor = system.actorOf(Props.create(Aggregator.class, (Creator<Aggregator>) () -> new Aggregator(services)), "aggregator");
        Future<?> res = Patterns.ask(actor, query, new Timeout(new FiniteDuration(30, TimeUnit.SECONDS)));
        try {
            return (List<SearchResponse>) Await.result(res, new FiniteDuration(30, TimeUnit.SECONDS));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            system.stop(actor);
        }
    }


    public static void main(String[] args) {
        Servers.withAllServers(() -> {
                    Main main = new Main();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    reader.lines().forEach((s) -> {
                        for (SearchResponse response : main.processQuery(s)) {
                            System.out.println(response.getName() + ": " + response.getUrl());
                        }
                    });
                    main.terminate();
                },
                Arrays.asList(
                        new Servers.StubServerConfig(Servers.Configuration.googlePort, Arrays.asList("1", "2", "3", "4"), 0),
                        new Servers.StubServerConfig(Servers.Configuration.yahooPort, Arrays.asList("1", "2", "3", "4"), 0),
                        new Servers.StubServerConfig(Servers.Configuration.bingPort, Arrays.asList("1", "2", "3", "4"), 0))
        );
    }

    public void terminate() {
        system.terminate();
    }
}
