package com.setravel.swifttravel.entities.output;

import lombok.Data;
import lombok.EqualsAndHashCode; // 如果继承了 HotelSummaryResponseDto
import java.util.List;

/**
 * 酒店详情响应 DTO (包含可用房型列表)
 */
@Data
@EqualsAndHashCode(callSuper = false) 
public class HotelDetailOutput extends HotelSummaryOutput { 
    private String description;
    // 在指定日期和人数下可用的房型
    private List<RoomInformationOutput> availableRooms; 
    // 看看需不需要加用户评论？
}

