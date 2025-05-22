package com.setravel.swifttravel.service;

import com.setravel.swifttravel.entities.*;

import java.util.List;

public interface NotificationService {
    /**
     * 发送购票成功信息
     * @param ticketsOrder
     */
    void sendTicketOrderPaySuccessMessage(TicketsOrders ticketsOrder);

    /**
     * 发送车票订单取消的消息
     * @param ticketsOrder
     */
    void sendTicketOrderCancelMessage(TicketsOrders ticketsOrder);

    /**
     * 发送发车提醒消息
     * @param ticketsOrder
     * @param train
     */
    void sendTrainDepartureReminder(TicketsOrders ticketsOrder, Trainnumbers train);

    /**
     * 获取未读消息
     * @param userId
     * @return
     */
    List<Notifications> getUnreadNotifications(byte[] userId);

    /**
     * 发送餐食购买成功的消息
     * @param foodOrder
     */
    void sendFoodOrderPaySuccessMessage(FoodOrders foodOrder);

    /**
     * 发送餐食取消的消息
     * @param foodOrder
     */
    void sendFoodOrderCancelMessage(FoodOrders foodOrder);

    /**
     * 发送酒店预订成功消息
     * @param hotelOrder
     */
    void sendHotelOrderPaySuccessMessage(HotelOrders hotelOrder);

    /**
     * 发送酒店订单取消消息
     * @param hotelOrder
     */
    void sendHotelOrderCancelMessage(HotelOrders hotelOrder);

    /**
     * 获取消息详情并标记为已读
     * @param messageId
     * @return
     */
    Result getMessageDetailAndMarkRead(byte[] messageId);

    /**
     * 将用户消息全部标记为已读
     * @param userId
     * @return
     */
    Result markAllAsRead(byte[] userId);

    /**
     * 分页查询
     * @param userId
     * @param page
     * @param size
     * @return
     */
    List<Notifications> getNotificationByPage(byte[] userId, int page, int size);

    /**
     * 消息数量
     * @param userId
     * @return
     */
    int countUserNotifications(byte[] userId);
}
