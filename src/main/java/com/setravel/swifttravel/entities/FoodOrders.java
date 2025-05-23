package com.setravel.swifttravel.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2025-04-23
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("food_orders")
public class FoodOrders extends Model<FoodOrders> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    @TableField("pay_id")
    private byte[] payId;

    @TableField("user_id")
    private byte[] userId;

    @TableField("del")
    private Boolean del;

    public static final String ID = "id";

    public static final String PAY_ID = "pay_id";

    public static final String USER_ID = "user_id";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

    public void setFoodOrderItems(List<FoodOrderItems> list) {

    }
}
