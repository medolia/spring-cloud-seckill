package zone.medolia.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import zone.medolia.common.domain.OrderInfo;
import zone.medolia.common.domain.SeckillUser;
import zone.medolia.common.result.CodeMsg;
import zone.medolia.common.result.Result;
import zone.medolia.common.vo.GoodsVo;
import zone.medolia.common.vo.OrderDetailVo;
import zone.medolia.order.client.GoodsClient;
import zone.medolia.order.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
    OrderService orderService;
    GoodsClient goodsClient;

    @Autowired
    public OrderController(OrderService orderService, GoodsClient goodsClient) {
        this.orderService = orderService;
        this.goodsClient = goodsClient;
    }

    @RequestMapping (value = "/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(@RequestParam("orderId") long orderId) {

        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) return Result.error(CodeMsg.ORDER_NOT_EXIST);

        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsClient.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
