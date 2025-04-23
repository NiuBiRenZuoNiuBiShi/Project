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
 * 车站表
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2024-01-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("station")
public class Station extends Model<Station> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    @TableField("station_name")
    private String stationName;

    @TableField("city_id")
    private byte[] cityId;

    @TableField("del")
    private Boolean del;

    public static final String ID = "id";

    public static final String STATION_NAME = "station_name";

    public static final String CITY_ID = "city_id";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
