package sd.ifmo.ru.twitter.model;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TweetParser {
    private final static DateFormat twitterDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z y");

    public static Tweet fromJSONString(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        try {
            return new Tweet(json.getLong("id"), twitterDateFormat.parse(json.getString("created_at")));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
