package com.setravel.swifttravel.service;

import java.time.LocalDate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.setravel.swifttravel.entities.Hotel;
import com.setravel.swifttravel.entities.output.HotelDetailOutput;
import com.setravel.swifttravel.entities.output.HotelSummaryOutput;
import com.setravel.swifttravel.entities.request.HotelSearchRequest;

public interface HotelService extends IService<Hotel> {
    /**
     * 搜索酒店并返回可用房型的概要信息
     */
    IPage<HotelSummaryOutput> searchAvailableHotels(HotelSearchRequest request);

    /**
     * 获取酒店详情及其在指定日期和人数下的可用房型
     */
    HotelDetailOutput getHotelDetailsWithAvailableRooms(String hotelIdString, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfPeople);
}
