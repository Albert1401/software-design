package sd.ifmo.ru.twitter.api;

import org.json.JSONArray;
import org.json.JSONObject;
import sd.ifmo.ru.twitter.model.Tweet;
import sd.ifmo.ru.twitter.model.TweetParser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TwitterApiImpl implements TwitterApi {


    private HttpClient client;

    public TwitterApiImpl(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        client = new TwitterApiOAuthHttpClient(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

    private List<Tweet> get(String url) {
        JSONArray tweets = new JSONObject(client.getBody(url)).getJSONArray("statuses");
        return StreamSupport.stream(tweets.spliterator(), false)
                .map(x -> TweetParser.fromJSONString(x.toString()))
                .collect(Collectors.toList());
    }

    public List<Tweet> searchLastTags(String tag, long maxId) {
        String url = new TwitterSearchRequestURLBuilder()
                .setMaxId(maxId)
                .setTag(tag)
                .build();
        return get(url);
    }

    public List<Tweet> searchLastTags(String tag) {
        String url = new TwitterSearchRequestURLBuilder()
                .setTag(tag)
                .build();
        return get(url);
    }
}