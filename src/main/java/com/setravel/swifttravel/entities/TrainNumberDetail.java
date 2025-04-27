package com.setravel.swifttravel.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class TrainNumberDetail {

    private String trainNumber;
    private List<String> stationLine;
    private List<LocalDateTime> timeLine;
    private List<LocalTime> waitingTimeLine;

    private List<Integer> business_coach;
    private List<Integer> first_coach;
    private List<Integer> second_coach;

    private List<Integer> business_seats_num;
    private List<Integer> first_seats_num;
    private List<Integer> second_seats_num;
    private Integer no_seats_num;

    private List<BigDecimal> business_price;
    private List<BigDecimal> first_price;
    private List<BigDecimal> second_price;
    private List<BigDecimal> no_seat_price;


    public String getLastStation() {
        return stationLine.getFirst();
    }

    public String getFirstStation() {
        return stationLine.getLast();
    }

    public LocalDateTime getLastTime() {
        return timeLine.getFirst();
    }

    public LocalDateTime getFirstTime() {
        return timeLine.getLast();
    }

}
