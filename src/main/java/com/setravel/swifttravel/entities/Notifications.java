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
 * 
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2024-01-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("notifications")
public class Notifications extends Model<Notifications> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    @TableId("message_id")
    private byte[] messageId;

    /**
     * 接收用户ID
     */
    @TableField("user_id")
    private byte[] userId;

    /**
     * 关联订单ID
     */
    @TableField("order_id")
    private byte[] orderId;

    /**
     * 消息类型
     */
    @TableField("message_type")
    private String messageType;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 发送渠道
     */
    @TableField("channel")
    private String channel;

    /**
     * 是否已读
     */
    @TableField("is_read")
    private Boolean read;

    /**
     * 发送状态
     */
    @TableField("send_status")
    private String sendStatus;

    /**
     * 发送时间
     */
    @TableField("send_time")
    private LocalDateTime sendTime;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("del")
    private Boolean del;

    public static final String MESSAGE_ID = "message_id";

    public static final String USER_ID = "user_id";

    public static final String ORDER_ID = "order_id";

    public static final String MESSAGE_TYPE = "message_type";

    public static final String CONTENT = "content";

    public static final String CHANNEL = "channel";

    public static final String IS_READ = "is_read";

    public static final String SEND_STATUS = "send_status";

    public static final String SEND_TIME = "send_time";

    public static final String CREATED_TIME = "created_time";

    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.messageId;
    }
}
