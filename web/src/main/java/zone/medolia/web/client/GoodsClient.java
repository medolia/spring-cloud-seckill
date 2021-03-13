package zone.medolia.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zone.medolia.common.vo.GoodsDetailVo;
import zone.medolia.common.vo.GoodsVo;

import java.util.List;

@FeignClient("goods")
public interface GoodsClient {

    @RequestMapping("/goods/to_list")
    List<GoodsVo> list();

    @RequestMapping(value = "/goods/to_detail/{goodsId}")
    GoodsDetailVo detail(@PathVariable("goodsId") long goodsId);
}
