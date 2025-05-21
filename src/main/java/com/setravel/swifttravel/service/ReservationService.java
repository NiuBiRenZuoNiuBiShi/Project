package com.setravel.swifttravel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.setravel.swifttravel.entities.Reservation;
import com.setravel.swifttravel.entities.output.HotelReserveOutput;
import com.setravel.swifttravel.entities.request.HotelReserveRequest;

public interface ReservationService extends IService<Reservation> {
    // 用户新建一个酒店预订记录
    HotelReserveOutput createReservation(HotelReserveRequest request);
    // 用户查询自己所有的历史订单
    IPage<HotelReserveOutput> getUserReservations(String userIdString, int pageNum, int pageSize);
    // 用户查询某一个具体的订单信息
    HotelReserveOutput getReservationDetails(String rsvIdString, String userIdString);
    // 用户取消自己的一个订单
    boolean cancelReservation(String rsvIdString, String userIdString);
}
