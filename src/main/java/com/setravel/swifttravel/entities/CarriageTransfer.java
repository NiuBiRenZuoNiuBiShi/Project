package com.setravel.swifttravel.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * 中转车次-前端车票查询
 */
@SuppressWarnings("unused")
public class CarriageTransfer {

    private LocalDateTime depTimeStart; // 第一趟始发时间
    private LocalDateTime arrTimeMiddle; // 第一趟到达时间
    private LocalTime transferTime; // 中转换乘时间
    private LocalDateTime depTimeMiddle; // 第二趟出发时间
    private LocalDateTime arrTimeEnd; // 第二趟到达时间
    private LocalTime costTime; // 车程总耗时
    private String trainNumber1;
    private String trainNumberId1;
    private String carriageId1;
    private String flag1;

    private String depCity; // 始发城市
    private String midCity; // 中转城市
    private String arrCity; // 终点城市

    private String depStation; // 始发站
    private String arrStation; // 终点站
    private String midStation1; // 中转站1程
    private String midStation2; // 中转站2程

    private Integer secondNumber1; // 1程二等座数量 (慢车硬座)
    private Integer firstNumber1; // 1程一等座数量 (慢车硬卧)
    private Integer businessNumber1; // 1程商务座数量(慢车软卧)
    private Integer noSeatNumber1; // 1程无座数量(慢车才有)

    private char type1; // 1程车类型 C 快 Z K 慢

    private BigDecimal secondPrice1; // 1程二等座价格 (慢车硬座价格)
    private BigDecimal firstPrice1; // 1程一等座价格 (慢车硬卧价格)
    private BigDecimal businessPrice1; // 1程商务座价格 (慢车软卧价格)
    private BigDecimal noSeatPrice1; // 1程无座价格 (慢车才有)

    private Integer secondNumber2; // 2程二等座数量 (慢车硬座)
    private Integer firstNumber2; // 2程一等座数量 (慢车硬卧)
    private Integer businessNumber2; // 2程商务座数量(慢车软卧)
    private Integer noSeatNumber2; // 2程无座数量(慢车才有)

    private char type2; // 2程车类型 C 快 Z K 慢

    private BigDecimal secondPrice2; // 2程二等座价格 (慢车硬座价格)
    private BigDecimal firstPrice2; // 2程一等座价格 (慢车硬卧价格)
    private BigDecimal businessPrice2; // 2程商务座价格 (慢车软卧价格)
    private BigDecimal noSeatPrice2; // 2程无座价格 (慢车才有)

    private String trainNumber2;
    private String trainNumberId2;
    private String carriageId2;
    private String flag2;

    private String message; // 换乘类型说明

    public void setFirstCarriage(CarriageThrough carriageThrough) {
        this.depTimeStart = carriageThrough.getDepTime();
        this.trainNumber1 = carriageThrough.getTrainNumber();
        this.trainNumberId1 = carriageThrough.getTrainNumberId();
        this.carriageId1 = carriageThrough.getCarriageId();
        this.arrTimeMiddle = carriageThrough.getArrTime();
        this.depCity = carriageThrough.getDepCity();
        this.midCity = carriageThrough.getArrCity();
        this.type1 = carriageThrough.getType();
        this.secondNumber1 = carriageThrough.getSecondNumber();
        this.firstNumber1 = carriageThrough.getFirstNumber();
        this.businessNumber1 = carriageThrough.getBusinessNumber();
        this.noSeatNumber1 = carriageThrough.getNoSeatNumber();
        this.secondPrice1 = carriageThrough.getSecondPrice();
        this.firstPrice1 = carriageThrough.getFirstPrice();
        this.businessPrice1 = carriageThrough.getBusinessPrice();
        this.noSeatPrice1 = carriageThrough.getNoSeatPrice();
        this.depStation = carriageThrough.getDepStation();
        this.midStation1 = carriageThrough.getArrStation();
        this.flag1 = carriageThrough.getFlag();
    }

    public void setSecondCarriage(CarriageThrough carriageThrough) {
        this.depTimeMiddle = carriageThrough.getDepTime();
        this.arrTimeEnd = carriageThrough.getArrTime();
        this.trainNumber2 = carriageThrough.getTrainNumber();
        this.trainNumberId2 = carriageThrough.getTrainNumberId();
        this.carriageId2 = carriageThrough.getCarriageId();
        this.arrCity = carriageThrough.getArrCity();
        this.type2 = carriageThrough.getType();
        this.secondNumber2 = carriageThrough.getSecondNumber();
        this.firstNumber2 = carriageThrough.getFirstNumber();
        this.businessNumber2 = carriageThrough.getBusinessNumber();
        this.noSeatNumber2 = carriageThrough.getNoSeatNumber();
        this.secondPrice2 = carriageThrough.getSecondPrice();
        this.firstPrice2 = carriageThrough.getFirstPrice();
        this.businessPrice2 = carriageThrough.getBusinessPrice();
        this.noSeatPrice2 = carriageThrough.getNoSeatPrice();
        this.midStation2 = carriageThrough.getDepStation();
        this.arrStation = carriageThrough.getArrStation();
        this.flag2 = carriageThrough.getFlag();
        this.costTime = LocalTime.ofSecondOfDay((int) (arrTimeEnd.toLocalTime().toSecondOfDay() - depTimeStart.toLocalTime().toSecondOfDay()));
        this.transferTime = LocalTime.ofSecondOfDay((int) (depTimeMiddle.toLocalTime().toSecondOfDay() - arrTimeMiddle.toLocalTime().toSecondOfDay()));
        if (Objects.equals(this.midStation1, this.midStation2)) {
            this.message = "同站换乘" + this.costTime;
        } else {
            this.message = "中转" + this.midStation1 + "站换乘" + this.midStation2 + "站" + this.costTime;
        }
    }
}
