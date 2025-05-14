package com.setravel.swifttravel.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 直达车次项-对应前端查询到的车次情况
 */
public class CarriageThrough {

    private LocalDateTime depTime; // 出发时间

    public LocalDateTime getDepTime() {
        return depTime;
    }

    public void setDepTime(LocalDateTime depTime) {
        this.depTime = depTime;
    }

    private LocalDateTime arrTime; // 到达时间

    public LocalDateTime getArrTime() {
        return arrTime;
    }

    public void setArrTime(LocalDateTime arrTime) {
        this.arrTime = arrTime;
    }

    private String depStation; // 始发站

    public String getDepStation() {
        return depStation;
    }

    public void setDepStation(String depStation) {
        this.depStation = depStation;
    }

    private String arrStation; // 终点站

    public String getArrStation() {
        return arrStation;
    }

    public void setArrStation(String arrStation) {
        this.arrStation = arrStation;
    }

    private String depCity; // 始发城市

    public void setDepCity(String depCity) {
        this.depCity = depCity;
    }

    public String getDepCity() {
        return depCity;
    }

    private String arrCity; // 终点城市

    public String getArrCity() {
        return arrCity;
    }

    public void setArrCity(String arrCity) {
        this.arrCity = arrCity;
    }

    private String trainNumber; // 车次号

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    private LocalDate costTime; // 车程时长

    public LocalDate getCostTime() {
        return costTime;
    }

    public void setCostTime(LocalDate costTime) {
        this.costTime = costTime;
    }

    private Integer secondNumber; // 二等座数量 (慢车硬座)

    public Integer getSecondNumber() {
        return secondNumber;
    }

    public void setSecondNumber(Integer secondNumber) {
        this.secondNumber = secondNumber;
    }

    private Integer firstNumber; // 一等座数量 (慢车硬卧)

    public Integer getFirstNumber() {
        return firstNumber;
    }

    public void setFirstNumber(Integer firstNumber) {
        this.firstNumber = firstNumber;
    }

    private Integer businessNumber; // 商务座数量(慢车软卧)

    public Integer getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(Integer businessNumber) {
        this.businessNumber = businessNumber;
    }

    private Integer noSeatNumber; // 无座数量(慢车才有)

    public Integer getNoSeatNumber() {
        return noSeatNumber;
    }

    public void setNoSeatNumber(Integer noSeatNumber) {
        this.noSeatNumber = noSeatNumber;
    }

    private char type; // 车类型 C 快 Z K 慢

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    private BigDecimal secondPrice; // 二等座价格 (慢车硬座价格)

    public BigDecimal getSecondPrice() {
        return secondPrice;
    }

    public void setSecondPrice(BigDecimal secondPrice) {
        this.secondPrice = secondPrice;
    }

    private BigDecimal firstPrice; // 一等座价格 (慢车硬卧价格)

    public BigDecimal getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(BigDecimal firstPrice) {
        this.firstPrice = firstPrice;
    }

    private BigDecimal businessPrice; // 商务座价格 (慢车软卧价格)

    public BigDecimal getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(BigDecimal businessPrice) {
        this.businessPrice = businessPrice;
    }

    private BigDecimal noSeatPrice; // 无座价格 (慢车才有)

    public BigDecimal getNoSeatPrice() {
        return noSeatPrice;
    }

    public void setNoSeatPrice(BigDecimal noSeatPrice) {
        this.noSeatPrice = noSeatPrice;
    }
}
