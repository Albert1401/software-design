package sd.ifmo.ru.twitter.api;

import java.util.Objects;

public class TwitterSearchRequestURLBuilder {
    private final static String serviceUrl = "https://api.twitter.com/1.1/search/tweets.json";
    private String tag;
    private int count = 100;
    private long maxId = -1;

    public TwitterSearchRequestURLBuilder setTag(String tag) {
        Objects.requireNonNull(tag);
        this.tag = tag;
        return this;
    }

    public TwitterSearchRequestURLBuilder setCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count cant be less than zero");
        }
        this.count = count;
        return this;
    }

    public TwitterSearchRequestURLBuilder setMaxId(long maxId) {
        if (maxId < 0) {
            throw new IllegalArgumentException("maxId cant be less than zero");
        }
        this.maxId = maxId;
        return this;
    }

    public String build() {
        if (tag == null || count == -1) {
            throw new IllegalStateException("illegal pis9");
        }
        return serviceUrl + "?" + "q=%23" + tag
                + "&include_entities=" + false
                + "&result_type=recent"
                + "&count=" + count
                + (maxId == -1 ? "" : "&max_id=" + maxId);
    }
}
