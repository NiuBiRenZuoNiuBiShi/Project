package com.setravel.swifttravel.entities.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> timeLine;

    @JsonFormat(pattern = "HH:mm:ss")
    private List<LocalTime> waitingTimeLine;

    /**
    *   这些属性的意思是，商务座在第几个车厢，一等座在第几个车厢
    */
    private List<Integer> business_coach;
    private List<Integer> first_coach;
    private List<Integer> second_coach;

    /**
    *   这些字段的作用是指名每个座车厢有多少个座位，
    *   比如说`business_seats_num`的长度和`business_coach`一样
    *   分别对应每个车厢有多少座位
     */
    private List<Integer> business_seats_num;
    private List<Integer> first_seats_num;
    private List<Integer> second_seats_num;
    private Integer no_seats_num;

    /**
    *   这些属性表示价格，长度和`stationLine`一样
    *   因此，每个属性的第一个值都应该是0
    * */
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
