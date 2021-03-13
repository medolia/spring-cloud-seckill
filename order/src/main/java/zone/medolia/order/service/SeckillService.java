package zone.medolia.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zone.medolia.common.domain.OrderInfo;
import zone.medolia.common.domain.SeckillOrder;
import zone.medolia.common.domain.SeckillUser;
import zone.medolia.common.vo.GoodsVo;
import zone.medolia.order.client.GoodsClient;
import zone.medolia.order.redis.RedisService;

@Service
@Slf4j
public class SeckillService {

    OrderService orderService;
    RedisService redisService;

    @Autowired
    public SeckillService(OrderService orderService, RedisService redisService) {
        this.orderService = orderService;
        this.redisService = redisService;
    }

    @Transactional
    public OrderInfo seckill(SeckillUser user, GoodsVo goods) {
        return orderService.createOrder(user, goods);
    }

    public long getSeckillResult(long userId, long goodsId) {
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        if (order != null)
            return order.getOrderId();
        else {
            return 0;
        }
    }
}
