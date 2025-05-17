package com.setravel.swifttravel.service.impl;

import com.setravel.swifttravel.entities.Notifications;
import com.setravel.swifttravel.entities.TicketsOrders;
import com.setravel.swifttravel.entities.Trainnumbers;
import com.setravel.swifttravel.mapper.NotificationsMapper;
import com.setravel.swifttravel.mapper.TrainNumberMapper;
import com.setravel.swifttravel.service.NotificationService;
import com.setravel.swifttravel.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.function.ToDoubleBiFunction;

import static com.setravel.swifttravel.entities.Notifications.*;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationsMapper notificationsMapper;
    @Autowired
    private TrainNumberMapper trainNumberMapper;

    /**
     * 发送支付成功消息
     * @param ticketsOrder
     */
    @Override
    public void sendTicketOrderPaySuccessMessage(TicketsOrders ticketsOrder) {
        Trainnumbers train = trainNumberMapper.selectById(ticketsOrder.getTrainId());
        String content = buildTicketContent(ticketsOrder, train, "支付成功，祝您旅途愉快！");
        insertNotification(ticketsOrder.getUserId(), ticketsOrder.getId(), PAY_SUCCESS, content);
    }

    /**
     * 发送取消订单消息
     * @param ticketsOrder
     */
    @Override
    public void sendTicketOrderCancelMessage(TicketsOrders ticketsOrder) {
        Trainnumbers train = trainNumberMapper.selectById(ticketsOrder.getTrainId());
        String content = buildTicketContent(ticketsOrder, train, "订单已取消，如有疑问请联系客服。");
        insertNotification(ticketsOrder.getUserId(), ticketsOrder.getId(), ORDER_CANCEL, content);
        //TODO
        /*取消订单后修改订单状态*/
    }

    /**
     * 发送发车前三分钟提醒
     * @param ticketsOrder
     * @param train
     */
    @Override
    public void sendTrainDepartureReminder(TicketsOrders ticketsOrder, Trainnumbers train) {
        String content = buildTicketContent(ticketsOrder, train, "列车将于3小时内发车，请您合理安排好您的出行时间。");
        insertNotification(ticketsOrder.getUserId(), ticketsOrder.getId(), DEP_REMINDER, content);
    }

    private void insertNotification(byte[] userId, byte[] orderId, String type, String content) {
        Notifications notification = new Notifications()
            .setMessageId(UUIDUtil.generateUUIDBytes())
            .setUserId(userId)
            .setOrderId(orderId)
            .setMessageType(type)
            .setContent(content)
            .setChannel(Notifications.INBOX)
            .setRead(false)
            .setSendStatus(Notifications.SENT)
            .setSendTime(LocalDateTime.now())
            .setCreatedTime(LocalDateTime.now())
            .setDel(false);

        notificationsMapper.insert(notification);
        log.info("已发送通知：{}", content);
    }

    private String buildTicketContent(TicketsOrders ticketsOrder, Trainnumbers train, String tailNote) {
        return String.format("您的车票信息如下：%s，%s %s开，%s-%s，%s次列车，座位：待补充，%s。%s",
            train.getTrainNumber(),
            train.getDepTime().toLocalDate(),
            train.getDepTime().toLocalTime(),
            train.getDepStation(),
            train.getArrStation(),
            train.getTrainNumber(),
            "成人票", //票类型、座位、车厢号等由seat补充
            tailNote);
    }
}
