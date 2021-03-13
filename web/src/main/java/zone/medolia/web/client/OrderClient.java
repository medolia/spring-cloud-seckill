package zone.medolia.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import zone.medolia.common.domain.OrderInfo;
import zone.medolia.common.domain.SeckillUser;
import zone.medolia.common.result.Result;
import zone.medolia.common.vo.OrderDetailVo;

@FeignClient("order")
public interface OrderClient {

    @RequestMapping(value = "/seckill/do_seckill", method = RequestMethod.POST)
    OrderInfo seckill(@RequestBody SeckillUser user, @RequestParam("goodsId") Long goodsId);

    @RequestMapping(value = "/order/detail", method = RequestMethod.GET)
    Result<OrderDetailVo> info(@RequestParam("orderId") Long orderId);

    /*@RequestMapping(value = "/seckill/result", method = RequestMethod.GET)
    Long seckillResult(@RequestParam("userId") long userId, @RequestParam("goodsId") long goodsId);*/

}
