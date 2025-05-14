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
import java.math.BigDecimal;

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
@TableName("food")
@Accessors(chain = true)
public class Food extends Model<Food> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    /**
     * which train this food belongs to
     */
    @TableField("trains_id")
    private byte[] trainsId;

    @TableField("food_name")
    private String foodName;

    @TableField("food_type")
    private String foodType;

    @TableField("price")
    private BigDecimal price;

    /**
     * 是否是午餐
     */
    @TableField("lunch")
    private Boolean lunch;

    @TableField("dinner")
    private Boolean dinner;

    @TableField("pic_url")
    private String picUrl;

    @TableField("del")
    private Boolean del;

    public static final String ID = "id";

    public static final String TRAINS_ID = "trains_id";

    public static final String FOOD_NAME = "food_name";

    public static final String FOOD_TYPE = "food_type";

    public static final String PRICE = "price";

    public static final String LUNCH = "lunch";

    public static final String DINNER = "dinner";

    public static final String PIC_URL = "pic_url";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
