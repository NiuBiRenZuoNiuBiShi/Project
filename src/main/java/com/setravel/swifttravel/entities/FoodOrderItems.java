package com.setravel.swifttravel.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 食物订单明细
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2024-01-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("food_order_items")
public class FoodOrderItems extends Model<FoodOrderItems> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    @TableField("order_id")
    private byte[] orderId;

    @TableField("food_id")
    private byte[] foodId;

    @TableField("food_number")
    private Integer foodNumber;

    public static final String ID = "id";

    public static final String ORDER_ID = "order_id";

    public static final String FOOD_ID = "food_id";

    public static final String FOOD_NUMBER = "food_number";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
