package com.setravel.swifttravel.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
@TableName("reservation")
public class Reservation extends Model<Reservation> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    @TableField("user_id")
    private byte[] userId;

    @TableField("hotel_id")
    private byte[] hotelId;

    @TableField("room_id")
    private byte[] roomId;

    @TableField("checkin_date")
    private LocalDate checkinDate;

    @TableField("checkout_date")
    private LocalDate checkoutDate;

    @TableField("status")
    private String status;

    @TableField("booking_time")
    private LocalDateTime bookingTime;

    @Version
    private Integer version;

    @TableField("del")
    private Boolean del;

    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String HOTEL_ID = "hotel_id";

    public static final String ROOM_ID = "room_id";

    public static final String CHECKIN_DATE = "checkin_date";

    public static final String CHECKOUT_DATE = "checkout_date";

    public static final String STATUS = "status";

    public static final String BOOKING_TIME = "booking_time";

    public static final String VERSION = "version";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
