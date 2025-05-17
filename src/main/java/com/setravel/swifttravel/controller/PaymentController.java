package com.setravel.swifttravel.controller;

import com.setravel.swifttravel.entities.PaymentRecords;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.request.CreatePaymentRequest;
import com.setravel.swifttravel.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 支付相关接口
 */
@RestController
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    /**
     * 创建支付记录
     * @param request
     * @return
     */
    @PostMapping("/create")
    public Result createPayment(@RequestBody CreatePaymentRequest request){
        return paymentService.createPayment(request);
    }

    /**
     * 完成支付，修改支付状态
     * @param id
     * @return
     */
    @PostMapping("/complete/{id}")
    public Result completePayment(@PathVariable String id){
        return paymentService.completePay(id);
    }
}
