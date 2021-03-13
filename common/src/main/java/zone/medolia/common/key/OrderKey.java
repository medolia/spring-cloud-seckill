package zone.medolia.common.key;

public class OrderKey extends BaseKeyPrefix {
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getSeckillOrderByUidGid = new OrderKey("soug");
}
