package zone.medolia.goods.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import zone.medolia.common.domain.SeckillGoods;
import zone.medolia.common.vo.GoodsVo;


import java.util.List;

@Mapper
public interface GoodsDao {

    // 得到商品列表
    @Select("select g.*,sg.stock_count, sg.start_date, sg.end_date,sg.seckill_price from seckill_goods sg left join goods g on sg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    // 得到单个商品详细信息
    @Select("select g.*,sg.stock_count, sg.start_date, sg.end_date,sg.seckill_price from seckill_goods sg left join goods g on sg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

}
