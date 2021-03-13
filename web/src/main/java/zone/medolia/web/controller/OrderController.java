package zone.medolia.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import zone.medolia.common.domain.SeckillUser;
import zone.medolia.common.result.CodeMsg;
import zone.medolia.common.result.Result;
import zone.medolia.common.vo.OrderDetailVo;
import zone.medolia.web.client.OrderClient;

@Controller
@RequestMapping("/order")
public class OrderController {
    OrderClient orderClient;

    @Autowired
    public void setOrderClient(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @RequestMapping(value = "/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(SeckillUser user, @RequestParam("orderId") long orderId) {
        if (user == null) return Result.error(CodeMsg.SESSION_ERROR);
        return orderClient.info(orderId);
    }
}
