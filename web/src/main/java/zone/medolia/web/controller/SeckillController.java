package zone.medolia.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import zone.medolia.common.domain.OrderInfo;
import zone.medolia.common.domain.SeckillUser;
import zone.medolia.common.result.CodeMsg;
import zone.medolia.common.result.Result;
import zone.medolia.web.client.OrderClient;
import zone.medolia.web.redis.RedisService;

@Controller
@RequestMapping("/seckill")
@Slf4j
public class SeckillController {

    OrderClient orderClient;
    RedisService redisService;
    RestTemplate restTemplate;

    @Autowired
    public SeckillController(OrderClient orderClient, RedisService redisService, RestTemplate restTemplate) {
        this.orderClient = orderClient;
        this.redisService = redisService;
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> seckill(SeckillUser user, @RequestParam("goodsId") long goodsId) {

        log.info("current user(web): {}", user);
        OrderInfo orderInfo = orderClient.seckill(user, goodsId);
        log.info("orderInfo(web): {}", orderInfo);
        if (orderInfo == null)
            return Result.error(CodeMsg.SECKILL_FAIL);

        return Result.success(orderInfo);
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(SeckillUser user, @RequestParam("goodsId") long goodsId) {

        long userId = user.getId();
        log.info("user currently finding order: {}, goodsId: {}", user, goodsId);
        Long result = restTemplate.getForObject("http://ORDER/seckill/result?userId="+
                userId+"&goodsId="+goodsId, Long.class);
        log.info("result: {}", result);

        // 成功返回订单查询结果，并不意味着秒杀成功
        return Result.success(result);
    }
}
