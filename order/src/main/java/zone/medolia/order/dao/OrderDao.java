package zone.medolia.order.dao;

import org.apache.ibatis.annotations.*;
import zone.medolia.common.domain.OrderInfo;
import zone.medolia.common.domain.SeckillGoods;
import zone.medolia.common.domain.SeckillOrder;
import zone.medolia.common.vo.GoodsVo;

import java.util.List;

@Mapper
public interface OrderDao {

    // 扣库存
    @Update("update seckill_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    public int reduceStock(SeckillGoods g);

    @Select("select * from seckill_order where user_id=#{userId} and goods_id=#{goodsId}")
    public SeckillOrder getSeckillOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    // 返回插入后新记录对应的 order id
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert("insert into seckill_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    public int insertSeckillOrder(SeckillOrder seckillOrder);

    // 获取订单信息
    @Select("select * from order_info where id = #{orderId}")
    public OrderInfo getOrderById(@Param("orderId") long orderId);

    @Select("select * from seckill_order where goods_id=#{goodsId}")
    public List<SeckillOrder> listByGoodsId(@Param("goodsId") long goodsId);
}
