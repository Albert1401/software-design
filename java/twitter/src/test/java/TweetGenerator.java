import sd.ifmo.ru.twitter.model.Tweet;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TweetGenerator {

    private final List<Tweet> tweets = new ArrayList<>();
    private final static DateFormat twitterDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z y");


    public TweetGenerator(int[] freqs, boolean next24Hours) {
        if (freqs.length != 24 || Arrays.stream(freqs).anyMatch(x -> x < 0)) {
            throw new IllegalArgumentException();
        }
        Random random = new Random();
        int next24 = random.nextInt(50) + 10;
        long[] times = new long[Arrays.stream(freqs).sum() + (next24Hours ? next24 : 0)];
        int pos = 0;

        long created = System.currentTimeMillis();
        for (int hour = 0; hour < 24; hour++) {
            for (int i = 0; i < freqs[hour]; i++) {
                int minute = random.nextInt(50) + 1;
                long twTime = created - 60 * 1000 * minute - 60 * 60 * 1000 * hour;
                times[pos++] = twTime;
            }
        }

        if (next24Hours) {
            for (int hour = 25; hour < 25 + next24; hour++) {
                int minute = random.nextInt(30);
                long twTime = created - 60 * 1000 * minute - 15 * 60 * 1000 - 60 * 60 * 1000 * hour;
                times[pos++] = twTime;
            }
        }

        Arrays.sort(times);
        for (int id = times.length - 1; id >= 0; id--) {
            tweets.add(new Tweet(id, new Date(times[id])));
        }
    }


    public String generateAnswer(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> qs = Arrays.stream(url.getQuery().split("&"))
                .collect(Collectors.toMap(v -> v.split("=")[0], v -> v.split("=")[1]));
        int count = Integer.parseInt(qs.get("count"));
        long maxId = Long.parseLong(qs.getOrDefault("max_id", Long.MAX_VALUE + ""));
        String q = qs.get("q");
        if (count < 0 || maxId < 0 || !q.startsWith("%23") || q.length() < 4) {
            throw new IllegalArgumentException();
        }
        String[] json = tweets.stream()
                .filter(tw -> tw.getId() < maxId)
                .limit(count)
                .map(tw -> "{\"id\":" + tw.getId()
                        + ", \"created_at\": \""
                        + twitterDateFormat.format(tw.getCreatedAt())
                        + "\"}")
                .toArray(String[]::new);
        return "{ \"statuses\" : " + Arrays.toString(json) + "}";
    }
}
