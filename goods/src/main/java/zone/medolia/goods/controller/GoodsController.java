package zone.medolia.goods.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zone.medolia.common.key.GoodsKey;
import zone.medolia.common.vo.GoodsDetailVo;
import zone.medolia.common.vo.GoodsVo;
import zone.medolia.goods.redis.RedisService;
import zone.medolia.goods.service.GoodsService;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/goods")
public class GoodsController implements InitializingBean {

    GoodsService goodsService;
    RedisService redisService;

    @Autowired
    public GoodsController(GoodsService goodsService, RedisService redisService) {
        this.goodsService = goodsService;
        this.redisService = redisService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) return;

        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getSeckillGoodsStock,
                    "" + goods.getId(), goods.getStockCount());
        }
    }

    @RequestMapping("/to_list")
    @ResponseBody
    public List<GoodsVo> list() {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        return goodsList;
    }

    @RequestMapping("/to_detail/{goodsId}")
    @ResponseBody
    public GoodsDetailVo detail(@PathVariable("goodsId") long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {
            seckillStatus = 0; // 秒杀未开始
            remainSeconds = (int) (startAt - now) / 1000;
        } else if (now > endAt) {
            seckillStatus = 2; // 秒杀已结束
            remainSeconds = -1;
        } else {
            seckillStatus = 1; // 秒杀进行中
            remainSeconds = 0;
        }
        GoodsDetailVo detailVo = new GoodsDetailVo();
        detailVo.setGoods(goods);
        // detailVo.setUser(user);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSeckillStatus(seckillStatus);

        return detailVo;
    }

    @RequestMapping("/vo/{goodsId}")
    @ResponseBody
    public GoodsVo getGoodsVoByGoodsId(@PathVariable("goodsId") long goodsId) {
        return goodsService.getGoodsVoByGoodsId(goodsId);
    }
}
