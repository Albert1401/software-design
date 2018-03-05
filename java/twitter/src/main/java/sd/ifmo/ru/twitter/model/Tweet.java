package sd.ifmo.ru.twitter.model;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Tweet {
    private long id;
    private Date createdAt;

    public Tweet(long id, Date createdAt) {
        Objects.requireNonNull(createdAt);
        this.id = id;
        this.createdAt = createdAt;
    }

    public int age() {
        long current = System.currentTimeMillis();
        return (int) TimeUnit.HOURS.convert(current - createdAt.getTime(), TimeUnit.MILLISECONDS);
    }

    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
