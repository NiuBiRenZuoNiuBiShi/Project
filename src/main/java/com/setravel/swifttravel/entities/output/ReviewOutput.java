package com.setravel.swifttravel.entities.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewOutput {
    private String reviewId;
    private String hotelId;
    private String userId;
    // 需要关联查询用户信息
    private String userName;
    private BigDecimal rating;
    private String comment;
    private LocalDateTime reviewTime;
}
