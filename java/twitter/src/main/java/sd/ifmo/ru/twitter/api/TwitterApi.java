package sd.ifmo.ru.twitter.api;

import sd.ifmo.ru.twitter.model.Tweet;

import java.util.List;

public interface TwitterApi {
    List<Tweet> searchLastTags(String tag, long maxId);

    List<Tweet> searchLastTags(String tag);
}
