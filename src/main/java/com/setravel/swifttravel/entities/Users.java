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
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2025-04-23
 */
@Getter
@Setter
@ToString
@TableName("users")
@Accessors(chain = true)
public class Users extends Model<Users> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private byte[] id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("id_card")
    private String idCard;

    @TableField("phone")
    private String phone;

    @TableField("email")
    private String email;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("del")
    private Boolean del;

    public static final String ID = "id";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String ID_CARD = "id_card";

    public static final String PHONE = "phone";

    public static final String EMAIL = "email";

    public static final String CREATED_AT = "created_at";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
