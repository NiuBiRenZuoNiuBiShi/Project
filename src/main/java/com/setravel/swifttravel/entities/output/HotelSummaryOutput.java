package com.setravel.swifttravel.entities.output;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 酒店概要信息响应 (用于列表展示)
 */
@Data
public class HotelSummaryOutput {
    private String id;
    private String name;
    private String address;
    private BigDecimal rating;
    private String picUrl;
    // 该酒店在指定日期范围内的最低可用房间价格
    private BigDecimal minPrice;
}