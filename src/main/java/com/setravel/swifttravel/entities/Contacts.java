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
 * 联系人表
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2025-04-23
 */
@Getter
@Setter
@ToString
@TableName("contacts")
@Accessors(chain = true)
public class Contacts extends Model<Contacts> {

    private static final long serialVersionUID = 1L;

    @TableId("contact_id")
    private byte[] contactId;

    @TableField("user_id")
    private byte[] userId;

    @TableField("contact_name")
    private String contactName;

    @TableField("contact_phone")
    private String contactPhone;

    @TableField("contact_email")
    private String contactEmail;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("del")
    private Boolean del;

    public static final String CONTACT_ID = "contact_id";

    public static final String USER_ID = "user_id";

    public static final String CONTACT_NAME = "contact_name";

    public static final String CONTACT_PHONE = "contact_phone";

    public static final String CONTACT_EMAIL = "contact_email";

    public static final String CREATED_AT = "created_at";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.contactId;
    }
}
