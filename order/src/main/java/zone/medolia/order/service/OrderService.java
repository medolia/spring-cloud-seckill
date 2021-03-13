package zone.medolia.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zone.medolia.common.domain.OrderInfo;
import zone.medolia.common.domain.SeckillGoods;
import zone.medolia.common.domain.SeckillOrder;
import zone.medolia.common.domain.SeckillUser;
import zone.medolia.common.vo.GoodsVo;
import zone.medolia.order.dao.OrderDao;

import java.util.Date;

@Service
@Slf4j
public class OrderService {

    OrderDao orderDao;

    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public SeckillOrder getSeckillOrderByUserIdGoodsId(long userId, long goodsId) {
        return orderDao.getSeckillOrderByUserIdGoodsId(userId, goodsId);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Transactional // 由 SeckillService 的事务方法调用，事务传播使其加入当前事务
    public OrderInfo createOrder(SeckillUser user, GoodsVo goods) {
        // 1. 减库存
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goods.getId());
        int stock = orderDao.reduceStock(g);
        log.info("num of affected row in db: " + stock);

        // 2. 插入订单详情表
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(user.getId());
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0); // 0 代表未支付状态
        orderDao.insert(orderInfo); // mybatis selectKey 调用后，id 会绑定在对象上

        // 3. 插入秒杀订单表
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        orderDao.insertSeckillOrder(seckillOrder);
        return orderInfo;
    }
}
