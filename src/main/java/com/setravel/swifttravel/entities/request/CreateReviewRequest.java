package com.setravel.swifttravel.entities.request;

import java.math.BigDecimal;
//import jakarta.validation.constraints.Max;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateReviewRequest {
    //@NotBlank(message = "酒店ID不能为空")
    private String hotelId;
    // @NotBlank(message = "用户ID不能为空")
    private String userId;
    // 关联的预订ID
    private String reservationId; 
    //@NotNull(message = "评分不能为空")
    //@Min(value = 1, message = "评分最低为1")
    //@Max(value = 5, message = "评分最高为5")
    private BigDecimal rating;
    // @Size(max = 1000, message = "评价内容不能超过1000字符")
    private String comment;
}
