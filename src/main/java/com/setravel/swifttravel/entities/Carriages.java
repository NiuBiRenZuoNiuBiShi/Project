package com.setravel.swifttravel.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.setravel.swifttravel.mapper.TrainNumberMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 车次
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2025-04-23
 */
@Getter
@Setter
@ToString
@TableName("carriages")
@Accessors(chain = true)
public class Carriages extends Model<Carriages> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    public byte[] getId() {
        return id;
    }

    /**
     * eg. G5151
     */
    @TableField("train_number")
    private byte[] trainNumber;

    /**
     * 出发站
     */
    @TableField("dep_station")
    private String depStation;

    /**
     * 出发城市
     */
    @TableField("dep_city")
    private String depCity;

    /**
     * 到达站
     */
    @TableField("arr_station")
    private String arrStation;

    /**
     * 到达城市
     */
    @TableField("arr_city")
    private String arrCity;

    /**
     * 到达时间
     */
    @TableField("arr_time")
    private LocalDateTime arrTime;

    /**
     * Time to Start this Carriage
     */
    @TableField("dep_time")
    private LocalDateTime depTime;

    /**
     * 等候时间
     */
    @TableField("wait_time")
    private LocalTime waitTime;

    /**
     * how many tickets or seats on the carriage
     */
    @TableField("all_number")
    private Integer allNumber;

    /**
     * how many first-class tickets or seats on the carriage
     */
    @TableField("first_number")
    private Integer firstNumber;

    @TableField("second_number")
    private Integer secondNumber;

    @TableField("business_number")
    private Integer businessNumber;

    @TableField("no_seat_number")
    private Integer noSeatNumber;

    @TableField("business_price")
    private BigDecimal businessPrice;

    @TableField("first_price")
    private BigDecimal firstPrice;

    @TableField("second_price")
    private BigDecimal secondPrice;

    @TableField("no_seat_price")
    private BigDecimal noSeatPrice;

    @TableField("flag")
    private byte[] flag;
    @TableField("del")
    private Boolean del;

    @TableField("version")
    @Version
    private Integer version;

    public static final String ID = "id";

    public static final String TRAIN_NUMBER = "train_number";

    public static final String DEP_STATION = "dep_station";

    public static final String DEP_CITY = "dep_city";

    public static final String ARR_STATION = "arr_station";

    public static final String ARR_CITY = "arr_city";

    public static final String ARR_TIME = "arr_time";

    public static final String WAIT_TIME = "wait_time";

    public static final String ALL_NUMBER = "all_number";

    public static final String FIRST_NUMBER = "first_number";

    public static final String SECOND_NUMBER = "second_number";

    public static final String BUSINESS_NUMBER = "business_number";

    public static final String NO_SEAT_NUMBER = "no_seat_number";

    public static final String BUSINESS_PRICE = "business_price";

    public static final String FIRST_PRICE = "first_price";

    public static final String SECOND_PRICE = "second_price";

    public static final String NO_SEAT_PRICE = "no_seat_price";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
