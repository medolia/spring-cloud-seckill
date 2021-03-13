package zone.medolia.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zone.medolia.common.vo.GoodsDetailVo;
import zone.medolia.common.vo.GoodsVo;

import java.util.List;

@FeignClient("goods")
@Component
public interface GoodsClient {

    @RequestMapping("/goods/vo/{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@PathVariable("goodsId")long goodsId);
}
