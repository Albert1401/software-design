package ru.ifmo.ctd.profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class MethodInfo{
    String name;
    List<Usage> usages = new ArrayList<>();

    public MethodInfo(String name) {
        this.name = name;
    }

    public static class Usage {
        public final long start;
        public final long duration;

        public Usage(long start, long duration) {
            this.start = start;
            this.duration = duration;
        }
    }

    private LongStream durations(){
        return usages.stream().mapToLong((x) -> x.duration);
    }

    public double avg(){
        return durations().average().orElse(-1);
    }

    public long all(){
        return durations().sum();
    }

    public long count(){
        return usages.size();
    }

    public long min(){
        return durations().min().orElse(-1);
    }

    public long max(){
        return durations().max().orElse(-1);
    }

}
