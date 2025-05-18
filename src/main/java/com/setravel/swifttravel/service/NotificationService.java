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
}
