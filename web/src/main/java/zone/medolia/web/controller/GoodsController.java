package zone.medolia.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zone.medolia.common.domain.SeckillUser;
import zone.medolia.common.result.CodeMsg;
import zone.medolia.common.result.Result;
import zone.medolia.common.vo.GoodsDetailVo;
import zone.medolia.common.vo.GoodsVo;
import zone.medolia.web.client.GoodsClient;

import java.util.List;

@Controller
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    GoodsClient goodsClient;

    @Autowired
    public void setGoodsClient(GoodsClient goodsClient) {
        this.goodsClient = goodsClient;
    }

    @RequestMapping("/to_list")
    public String list(Model model) {
        List<GoodsVo> goodsList = goodsClient.list();
        log.info("goodsList: {}", goodsList);
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping(value = "/to_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(SeckillUser user, @PathVariable("goodsId") long goodsId) {

        GoodsDetailVo goodsDetailVo = goodsClient.detail(goodsId);
        goodsDetailVo.setUser(user);
        log.info("goods detail: {}", goodsDetailVo);
        return Result.success(goodsDetailVo);
    }
}
