package com.setravel.swifttravel.entities.request;

import java.time.LocalDate;
import lombok.Data;

/*
 * 创建预订请求
 */
@Data
public class HotelReserveRequest {
    private String userId;
    private String hotelId;
    private String roomType;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Integer numberPeople;
}
