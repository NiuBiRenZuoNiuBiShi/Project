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
import java.time.LocalDate;

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
@TableName("room")
@Accessors(chain = true)
public class Room extends Model<Room> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    @TableField("hotel_id")
    private byte[] hotelId;

    @TableField("room_type")
    private String roomType;

    @TableField("price")
    private BigDecimal price;

    @TableField("room_date")
    private LocalDate roomDate;

    @TableField("availability")
    private Boolean availability;

    @TableField("del")
    private Boolean del;

    public static final String ID = "id";

    public static final String HOTEL_ID = "hotel_id";

    public static final String ROOM_TYPE = "room_type";

    public static final String PRICE = "price";

    public static final String ROOM_DATE = "room_date";

    public static final String AVAILABILITY = "availability";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
