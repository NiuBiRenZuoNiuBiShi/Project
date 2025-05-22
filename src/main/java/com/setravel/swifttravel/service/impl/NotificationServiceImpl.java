package com.setravel.swifttravel.service.impl;

import com.setravel.swifttravel.entities.*;
import com.setravel.swifttravel.mapper.*;
import com.setravel.swifttravel.service.NotificationService;
import com.setravel.swifttravel.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

import static com.setravel.swifttravel.entities.Notifications.*;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationsMapper notificationsMapper;
    @Autowired
    private TrainNumberMapper trainNumberMapper;
    @Autowired
    private FoodMapper foodMapper;
    @Autowired
    private FoodOrdersMapper foodOrdersMapper;
    @Autowired
    private FoodOrderItemsMapper foodOrderItemsMapper;
    @Autowired
    private HotelMapper hotelMapper;
    @Autowired
    private HotelOrderMapper hotelOrderMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private ReservationMapper reservationMapper;

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

    /**
     * 获取所有未读消息
     * @param userId
     * @return
     */
    @Override
    public List<Notifications> getUnreadNotifications(byte[] userId) {
        return notificationsMapper.selectUnreadByUserId(userId);
    }

    /**
     * 发送餐食预订成功消息
     * @param foodOrder
     */
    @Override
    public void sendFoodOrderPaySuccessMessage(FoodOrders foodOrder) {
        String content = buildFoodContent(foodOrder, "您的餐食已预订成功，我们将准时为您配送");
        insertNotification(foodOrder.getUserId(), foodOrder.getId(), PAY_SUCCESS, content);
    }

    /**
     * 发送餐食取消消息
     * @param foodOrder
     */
    @Override
    public void sendFoodOrderCancelMessage(FoodOrders foodOrder) {
        String content = buildFoodContent(foodOrder, "您的餐食已取消，欢迎下次订购");
        insertNotification(foodOrder.getUserId(), foodOrder.getId(), ORDER_CANCEL, content);
    }

    /**
     * 发送酒店预订成功消息
     * @param hotelOrder
     */
    @Override
    public void sendHotelOrderPaySuccessMessage(HotelOrders hotelOrder) {
        String content = buildHotelOrderContent(hotelOrder, "酒店预订成功，祝您入住愉快！");
        insertNotification(hotelOrder.getUserId(), hotelOrder.getId(), PAY_SUCCESS, content);
    }

    /**
     * 发送酒店订单取消消息
     * @param hotelOrder
     */
    @Override
    public void sendHotelOrderCancelMessage(HotelOrders hotelOrder) {
        String content = buildHotelOrderContent(hotelOrder, "您的酒店订单已取消，如有疑问请联系客服。");
        insertNotification(hotelOrder.getUserId(), hotelOrder.getId(), ORDER_CANCEL, content);
    }

    @Override
    public Result getMessageDetailAndMarkRead(byte[] messageId) {
        Notifications notification = notificationsMapper.selectById(messageId);
        if(notification == null) {
            return Result.error("消息不存在");
        }
        if(!notification.getRead()){
            notification.setRead(true);
            notificationsMapper.updateById(notification);
        }
        return Result.success("消息详情", notification);
    }

    @Override
    public Result markAllAsRead(byte[] userId) {
        int updated = notificationsMapper.markAllAsRead(userId);
        return Result.success("已将消息全部标记为已读，共更新：" + updated + "条");
    }

    @Override
    public List<Notifications> getNotificationByPage(byte[] userId, int page, int size) {
        int offset = (page - 1) * size;
        return notificationsMapper.selectPagedByUserId(userId, offset, size);
    }

    @Override
    public int countUserNotifications(byte[] userId) {
        return notificationsMapper.countByUserId(userId);
    }

    /**
     * 插入一条消息
     * @param userId
     * @param orderId
     * @param type
     * @param content
     */
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

        try {
            notificationsMapper.insert(notification);
            log.info("已发送通知：{}", content);
        }catch (DuplicateKeyException e){
            log.warn("通知已存在，未重复发送");
        }
    }

    /**
     * 车票的具体消息
     * @param ticketsOrder
     * @param train
     * @param tailNote
     * @return
     */
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

    /**
     * 餐食的具体消息
     * @param foodOrders
     * @param tailNote
     * @return
     */
    private String buildFoodContent(FoodOrders foodOrders, String tailNote){
        List<FoodOrderItems> items = foodOrderItemsMapper.selectByOrderId(foodOrders.getId());

        StringBuilder sb = new StringBuilder();
        sb.append("您订购的餐食如下：\n");

        for(FoodOrderItems item : items){
            Food food = foodMapper.selectById(item.getFoodId());
            if(food != null){
                sb.append(String.format("- %s × %d份（¥%s）\\n",
                    food.getFoodName(),
                    item.getFoodNumber(),
                    food.getPrice().multiply(BigDecimal.valueOf(item.getFoodNumber()))));
            }
        }
        sb.append(tailNote);
        return sb.toString();
    }

    /**
     * 酒店消息内容
     * @param hotelOrder
     * @param tailNote
     * @return
     */
    private String buildHotelOrderContent(HotelOrders hotelOrder, String tailNote) {
        Reservation res = reservationMapper.selectById(hotelOrder.getReservationId());
        if (res == null) return "未找到预订信息";

        Hotel hotel = hotelMapper.selectById(res.getHotelId());
        Room room = roomMapper.selectById(res.getRoomId());

        return String.format("""
        您预订的酒店信息如下：
        - 酒店：%s
        - 地址：%s
        - 房型：%s
        - 入住时间：%s
        - 离店时间：%s
        - 预订状态：%s
        %s
        """,
            hotel != null ? hotel.getName() : "未知酒店",
            hotel != null ? hotel.getAddress() : "未知地址",
            room != null ? room.getRoomType() : "未知房型",
            res.getCheckinDate(),
            res.getCheckoutDate(),
            res.getStatus(),
            tailNote
        );
    }
}
