package zone.medolia.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import zone.medolia.common.vo.GoodsVo;
import zone.medolia.order.client.GoodsClient;

@Slf4j
@Controller
public class TestController {

    GoodsClient goodsClient;
    RestTemplate restTemplate;

    @Autowired
    public TestController(GoodsClient goodsClient, RestTemplate restTemplate) {
        this.goodsClient = goodsClient;
        this.restTemplate = restTemplate;
    }

    /*@RequestMapping("/goods/vo/{goodsId}")
    @ResponseBody
    public GoodsVo getGoodsVoByGoodsId(@PathVariable("goodsId")long goodsId) {
        return goodsClient.getGoodsVoByGoodsId(goodsId);
    }*/

    @RequestMapping("/test/vo/{goodsId}")
    @ResponseBody
    public GoodsVo getGoodsVoByGoodsId(@PathVariable("goodsId")long goodsId) {
        GoodsVo response = restTemplate.getForObject("http://GOODS/goods/vo/"+goodsId, GoodsVo.class);
        log.info("response from restTemplate: {}", response);
        return response;
    }
}
