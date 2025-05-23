package com.setravel.swifttravel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.setravel.swifttravel.entities.*;
import com.setravel.swifttravel.entities.request.CreatePaymentRequest;
import com.setravel.swifttravel.mapper.*;
import com.setravel.swifttravel.security.UserContext;
import com.setravel.swifttravel.service.NotificationService;
import com.setravel.swifttravel.service.PaymentService;
import com.setravel.swifttravel.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.setravel.swifttravel.entities.PaymentRecords.*;

@Service
@Slf4j
public class PaymentServiceImpl
    extends ServiceImpl<PaymentRecordsMapper, PaymentRecords>
    implements PaymentService {

    @Autowired
    private PaymentRecordsMapper paymentRecordsMapper;
    @Autowired
    private TicketOrdersMapper ticketOrdersMapper;
    @Autowired
    private HotelOrderMapper hotelOrderMapper;
    @Autowired
    private FoodOrdersMapper foodOrdersMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserMapper userMapper;

    /**
     * 创建支付订单
     * @param request
     * @return
     */
    //选择支付方式后创建该订单
    @Override
    public Result createPayment(CreatePaymentRequest request) {
            UUID paymentUUID = UUID.randomUUID();
            byte[] paymentId = UUIDUtil.uuidToBytes(paymentUUID);

            PaymentRecords record = new PaymentRecords()
                .setPaymentId(paymentId)
                .setOrderId(UUIDUtil.uuidToBytes(UUID.fromString(request.getOrderId())))
                .setUserId(UserContext.getCurrentUserId(userMapper))
                .setPayType(request.getPayType())
                .setAmount(request.getAmount())
                .setMethod(request.getMethod())
                .setStatus(PaymentRecords.TO_BE_PAID)
                .setCreatedTime(LocalDateTime.now())
                .setDel(false);

        try{
            paymentRecordsMapper.insert(record);
            return Result.success("创建支付成功", paymentUUID.toString());
        }catch (Exception e){
            log.error("创建支付失败，准备回写支付状态", e);

            try{
                record.setStatus(PaymentRecords.PAY_FAILED);
                record.setUpdatedTime(LocalDateTime.now());
                paymentRecordsMapper.updateById(record);
            }catch (Exception ex){
                log.error("支付失败状态回写也失败", ex);
            }

            return Result.error("创建支付失败：" + e.getMessage());
        }
    }

    /**
     * 完成订单支付，修改支付状态
     * @param id
     * @return
     */
    @Override
    public Result completePay(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            byte[] paymentId = UUIDUtil.uuidToBytes(uuid);

            PaymentRecords record = paymentRecordsMapper.findByPaymentId(paymentId);

            if (record == null) {
                return Result.error("支付记录不存在");
            }
            if (PaymentRecords.PAY_SUCCESS.equals(record.getStatus())) {
                return Result.success("该订单已完成支付", record);
            }
            if (!TO_BE_PAID.equals(record.getStatus())) {
                return Result.error("当前支付状态不允许再次支付");
            }

            record.setStatus(PAY_SUCCESS);
            record.setUpdatedTime(LocalDateTime.now());

            int result = paymentRecordsMapper.updateById(record);
            if (result > 0) {
                //发送对应的消息
                String type = record.getPayType();
                if(TICKET.equals(type)){
                    TicketsOrders order = ticketOrdersMapper.selectById(record.getOrderId());
                    try {
                        notificationService.sendTicketOrderPaySuccessMessage(order);
                    }catch (Exception e){
                        log.warn("发送支付成功通知失败：{}", e.getMessage());
                    }
                }else if (HOTEL.equals(type)) {
                    HotelOrders order = hotelOrderMapper.selectById(record.getOrderId());
                    try {
                        notificationService.sendHotelOrderPaySuccessMessage(order);
                    }catch (Exception e){
                        log.warn("发送支付成功通知失败：{}", e.getMessage());
                    }
                } else if (FOOD.equals(type)) {
                    FoodOrders order = foodOrdersMapper.selectById(record.getOrderId());
                    try {
                        notificationService.sendFoodOrderPaySuccessMessage(order);
                    }catch (Exception e){
                        log.warn("发送支付成功通知失败：{}", e.getMessage());
                    }
                }

                return Result.success("支付成功", record);
            } else {
                return Result.error("支付记录更新失败");
            }
        } catch (IllegalArgumentException e) {
            return Result.error("无效的支付 ID");
        } catch (Exception e) {
            return Result.error("支付异常：" + e.getMessage());
        }
    }
}
