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
 * 城市表
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2024-01-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("city")
public class City extends Model<City> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    @TableField("city_name")
    private String cityName;

    @TableField("del")
    private Boolean del;

    public static final String ID = "id";

    public static final String CITY_NAME = "city_name";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
