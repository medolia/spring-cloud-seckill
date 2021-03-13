package zone.medolia.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import zone.medolia.common.domain.OrderInfo;
import zone.medolia.common.domain.SeckillOrder;
import zone.medolia.common.domain.SeckillUser;
import zone.medolia.common.key.GoodsKey;
import zone.medolia.common.key.SeckillUserKey;
import zone.medolia.common.result.CodeMsg;
import zone.medolia.common.result.Result;
import zone.medolia.common.vo.GoodsVo;
import zone.medolia.order.client.GoodsClient;
import zone.medolia.order.redis.RedisService;
import zone.medolia.order.service.OrderService;
import zone.medolia.order.service.SeckillService;

@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    RedisService redisService;
    OrderService orderService;
    SeckillService seckillService;
    GoodsClient goodsClient;
    RestTemplate restTemplate;

    @Autowired
    public SeckillController(RedisService redisService, OrderService orderService,
                             SeckillService seckillService, GoodsClient goodsClient,
                             RestTemplate restTemplate) {
        this.redisService = redisService;
        this.orderService = orderService;
        this.seckillService = seckillService;
        this.goodsClient = goodsClient;
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public OrderInfo seckill(@RequestBody SeckillUser user, @RequestParam("goodsId") long goodsId) {

        if (user == null) return null;
        log.info("current user in order service: {}", user);

        // 预减库存
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        log.info("stock obtained from redis: " + stock);
        if (stock < 0) {
            log.info("insufficient stock in redis: {}", stock);
            return null;
        }

        // 判断重复秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        log.info("order query before seckill: {}", order);
        if (order!= null && order.getId() > 0) {
            log.info("seckill repeated! orderId: {}", order.getId());
            return null;
        }
        log.info("进入秒杀事务流程");

        // 秒杀
        // todo 发送秒杀请求至消息队列
        log.info("ready to get goodVo");
        GoodsVo goodsVo = restTemplate.getForObject("http://GOODS/goods/vo/"+goodsId, GoodsVo.class);
        log.info("goodsVo: {}", goodsVo);
        OrderInfo orderInfo = seckillService.seckill(user, goodsVo);
        log.info("orderInfo: {}", orderInfo);

        return orderInfo;
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Long seckillResult(@RequestParam("userId") long userId, @RequestParam("goodsId") long goodsId) {
        Long result = seckillService.getSeckillResult(userId, goodsId);
        log.info("seckill query result: {}", result);
        return result;
    }
}
