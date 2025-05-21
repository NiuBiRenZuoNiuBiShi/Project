package com.setravel.swifttravel.entities.output;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 酒店概要信息响应 (用于列表展示)
 */
@Data
public class HotelSummaryOutput {
    private String id; // 应该不需要HotelID吧
    private String name;
    private String address;
    private BigDecimal rating;
    private String picUrl;
    private BigDecimal minPrice; // 该酒店在指定日期范围内的最低可用房间价格
}