package zone.medolia.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zone.medolia.common.vo.GoodsVo;
import zone.medolia.goods.dao.GoodsDao;

import java.util.List;

@Service
public class GoodsService {

    private GoodsDao goodsDao;

    @Autowired
    public void setGoodsDao(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }
}
