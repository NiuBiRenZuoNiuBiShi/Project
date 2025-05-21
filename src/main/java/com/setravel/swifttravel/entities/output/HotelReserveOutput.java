package com.setravel.swifttravel.entities.output;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class HotelReserveOutput {
    private String id; // 订单ID
    private String userId;
    private String hotelId;
    private String userName;
    private String hotelName; // 酒店名称
    private String roomType; // 房间类型
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String status; // 订单状态
    private LocalDateTime reserveTime;
    private BigDecimal totalPrice; // 总价，需要计算
}
