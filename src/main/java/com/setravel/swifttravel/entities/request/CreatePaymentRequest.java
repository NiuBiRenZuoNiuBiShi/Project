package com.setravel.swifttravel.entities.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 前端请求数据-创建订单
 */
@Data
public class CreatePaymentRequest {
    private byte[] orderId;
    private byte[] userId;
    private String payType;     //TICKET  HOTEL  MEAL
    private BigDecimal amount;
    private String method;  //WECHAT  ALIPAY  BANK
}
