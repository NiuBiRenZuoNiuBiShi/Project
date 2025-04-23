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
@TableName("hotel")
public class Hotel extends Model<Hotel> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    @TableField("name")
    private String name;

    @TableField("address")
    private String address;

    @TableField("rating")
    private BigDecimal rating;

    @TableField("description")
    private String description;

    @TableField("pic_url")
    private String picUrl;

    @TableField("del")
    private Boolean del;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String ADDRESS = "address";

    public static final String RATING = "rating";

    public static final String DESCRIPTION = "description";

    public static final String PIC_URL = "pic_url";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
