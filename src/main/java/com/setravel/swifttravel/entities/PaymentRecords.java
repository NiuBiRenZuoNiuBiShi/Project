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

/**
 * <p>
 * 
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2025-04-23
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("payment_records")
public class PaymentRecords extends Model<PaymentRecords> {

    private static final long serialVersionUID = 1L;

    /**
     * 支付ID
     */
    @TableId("payment_id")
    private byte[] paymentId;

    /**
     * 关联订单ID
     */
    @TableField("order_id")
    private byte[] orderId;

    /**
     * 支付用户ID
     */
    @TableField("user_id")
    private byte[] userId;

    /**
     * 购买对象类型
     */
    @TableField("pay_type")
    private String payType;

    /**
     * 支付金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 支付方式
     */
    @TableField("method")
    private String method;

    /**
     * 支付状态
     */
    @TableField("status")
    private String status;

    /**
     * 发起支付时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 最后更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableField("del")
    private Boolean del;

    public static final String PAYMENT_ID = "payment_id";

    public static final String ORDER_ID = "order_id";

    public static final String USER_ID = "user_id";

    public static final String PAY_TYPE = "pay_type";

    public static final String AMOUNT = "amount";

    public static final String METHOD = "method";

    public static final String STATUS = "status";

    public static final String CREATED_TIME = "created_time";

    public static final String UPDATED_TIME = "updated_time";

    public static final String DEL = "del";

    public static final String PAY_FAILED = "支付失败";
    public static final String PAY_SUCCESS = "支付成功";
    public static final String TO_BE_PAID = "待支付";

    @Override
    public Serializable pkVal() {
        return this.paymentId;
    }
}
