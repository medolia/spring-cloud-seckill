package zone.medolia.common.key;

public interface KeyPrefix {
    int expireSeconds();

    String getPrefix();
}
