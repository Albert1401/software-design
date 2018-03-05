import sd.ifmo.ru.twitter.api.TwitterApi;
import sd.ifmo.ru.twitter.model.Tweet;

import java.util.List;
import java.util.Objects;

public class HotTags {
    private TwitterApi twitter;


    public HotTags(TwitterApi twitter) {
        Objects.requireNonNull(twitter);
        this.twitter = twitter;
    }


    public int[] getFrequencies(String tag) {
        boolean isTweetsNew = true;
        int[] freqs = new int[24];

        long maxId = -1;
        while (isTweetsNew) {
            List<Tweet> tweets;
            if (maxId == -1) {
                tweets = twitter.searchLastTags(tag);
            } else {
                tweets = twitter.searchLastTags(tag, maxId);
            }

            for (Tweet tweet : tweets) {
                if (tweet.age() >= 24) {
                    isTweetsNew = false;
                    break;
                }
                freqs[tweet.age()] += 1;
                if (maxId == -1) {
                    maxId = tweet.getId();
                } else {
                    maxId = Math.min(maxId, tweet.getId());
                }
            }
            if (tweets.isEmpty()) {
                break;
            }
        }
        return freqs;
    }
}
