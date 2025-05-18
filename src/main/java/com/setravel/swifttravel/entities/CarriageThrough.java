package com.setravel.swifttravel.entities;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 直达车次项-对应前端查询到的车次情况
 */
@Getter
@Setter
public class CarriageThrough {
    private byte[] id; // 车次ID

    private LocalDateTime depTime; // 出发时间

    private LocalDateTime arrTime; // 到达时间

    private String depStation; // 始发站

    private String arrStation; // 终点站

    private String depCity; // 始发城市

    private String arrCity; // 终点城市

    private String trainNumber; // 车次号

    private LocalDate costTime; // 车程时长

    private Integer secondNumber; // 二等座数量 (慢车硬座)

    private Integer firstNumber; // 一等座数量 (慢车硬卧)

    private Integer businessNumber; // 商务座数量(慢车软卧)

    private Integer noSeatNumber; // 无座数量(慢车才有)

    private char type; // 车类型 C 快 Z K 慢

    private BigDecimal secondPrice; // 二等座价格 (慢车硬座价格)

    private BigDecimal firstPrice; // 一等座价格 (慢车硬卧价格)

    private BigDecimal businessPrice; // 商务座价格 (慢车软卧价格)

    private BigDecimal noSeatPrice; // 无座价格 (慢车才有)
}
