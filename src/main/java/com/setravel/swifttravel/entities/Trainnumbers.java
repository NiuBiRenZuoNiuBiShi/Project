package com.setravel.swifttravel.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 车次表
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2024-01-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("trainnumbers")
public class Trainnumbers extends Model<Trainnumbers> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    @TableField("train_number")
    private String trainNumber;

    @TableField("dep_station")
    private String depStation;

    @TableField("dep_city")
    private String depCity;

    @TableField("arr_station")
    private String arrStation;

    @TableField("arr_city")
    private String arrCity;

    @TableField("dep_time")
    private LocalDateTime depTime;

    @TableField("arr_time")
    private LocalDateTime arrTime;

    @TableField("del")
    private Boolean del;

    public static final String ID = "id";

    public static final String TRAIN_NUMBER = "train_number";

    public static final String DEP_STATION = "dep_station";

    public static final String DEP_CITY = "dep_city";

    public static final String ARR_STATION = "arr_station";

    public static final String ARR_CITY = "arr_city";

    public static final String DEP_TIME = "dep_time";

    public static final String ARR_TIME = "arr_time";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
