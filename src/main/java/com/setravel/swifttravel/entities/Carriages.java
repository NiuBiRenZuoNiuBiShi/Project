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

    @TableField
    private Double businessPrice;

    @TableField
    private Double firstPrice;

    @TableField
    private Double secondPrice;

    @TableField
    private Double noSeatPrice;

    @TableField("del")
    private Boolean del;

    /**
     * 商务座票价
     */
    @TableField("business_price")
    private BigDecimal businessPrice;

    /**
     * 一等座票价
     */
    @TableField("first_price")
    private BigDecimal firstPrice;

    /**
     * 二等座票价
     */
    @TableField("second_price")
    private BigDecimal secondPrice;

    /**
     * 无座票价
     */
    @TableField("no_seat_price")
    private BigDecimal noSeatPrice;

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

    public static final String BUSINESS_PRICE = "business_price";

    public static final String FIRST_PRICE = "first_price";

    public static final String SECOND_PRICE = "second_price";

    public static final String NO_SEAT_PRICE = "no_seat_price";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
