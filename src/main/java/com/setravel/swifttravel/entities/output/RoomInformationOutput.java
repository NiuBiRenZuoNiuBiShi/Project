package com.setravel.swifttravel.entities.output;

import lombok.Data;
import java.math.BigDecimal;

/*
 * 房间信息响应
 */
@Data
public class RoomInformationOutput {
    private String id; // Room ID
    private String roomType;
    private BigDecimal price;
    private Integer capacity; // 房间容量

}
