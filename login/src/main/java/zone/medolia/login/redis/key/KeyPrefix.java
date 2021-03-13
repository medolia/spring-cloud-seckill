package zone.medolia.login.redis.key;

public interface KeyPrefix {
    int expireSeconds();

    String getPrefix();
}
