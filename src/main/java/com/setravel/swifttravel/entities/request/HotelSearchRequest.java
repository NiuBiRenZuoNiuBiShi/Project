package com.setravel.swifttravel.entities.request;

import java.time.LocalDate;

import lombok.Data;

/*
 * 酒店搜索请求
 * 用户提供信息搜索
 */
@Data
public class HotelSearchRequest {
    private String location; // 搜索位置 (可对应酒店地址的部分匹配)
    private String hotelName; // 酒店名称 (模糊匹配)
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Integer numberOfPeople; // 人数 (用于匹配房间容量)

    // 分页参数(假设的)
    private int pageNum = 1;
    private int pageSize = 10; 
}
