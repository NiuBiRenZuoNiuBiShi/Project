package com.setravel.swifttravel.service;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.request.CreatePaymentRequest;

public interface PaymentService {
    /**
     * 创建支付订单
     * @param request
     * @return
     */
    Result createPayment(CreatePaymentRequest request);

    /**
     * 完成支付，修改支付状态
     * @param id
     * @return
     */
    Result completePay(String id);

    /**
     * 取消支付
     * @param paymentId
     * @return
     */
    Result cancelPayment(String paymentId);
}
