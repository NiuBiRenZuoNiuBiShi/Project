package com.setravel.swifttravel.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2024-01-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("seats")
public class Seats extends Model<Seats> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    @TableField("train_number")
    private byte[] trainNumber;

    /**
     * 车厢号
     */
    @TableField("coach")
    private Integer coach;

    @TableField("seat_type")
    private String seatType;

    /**
     * 座位状态
     */
    @TableField("flags")
    private byte[] flags;

    /**
     * 乐观锁
     */
    @TableField("version")
    private Integer version;

    @TableField("del")
    private Boolean del;

    public static final String ID = "id";

    public static final String TRAIN_NUMBER = "train_number";

    public static final String COACH = "coach";

    public static final String SEAT_TYPE = "seat_type";

    public static final String PRICE = "price";

    public static final String FLAGS = "flags";

    public static final String VERSION = "version";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
