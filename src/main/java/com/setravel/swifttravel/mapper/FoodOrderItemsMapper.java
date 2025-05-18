package com.setravel.swifttravel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.FoodOrderItems;
import com.setravel.swifttravel.entities.FoodOrders;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FoodOrderItemsMapper extends BaseMapper<FoodOrderItems> {
    @Select("SELECT * FROM food_order_items WHERE order_id = #{orderId}")
    List<FoodOrderItems> selectByOrderId(byte[] orderId);
}
