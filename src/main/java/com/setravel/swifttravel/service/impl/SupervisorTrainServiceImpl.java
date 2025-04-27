package com.setravel.swifttravel.service.impl;

import com.setravel.swifttravel.entities.*;
import com.setravel.swifttravel.mapper.CarriagesMapper;
import com.setravel.swifttravel.mapper.SeatsMapper;
import com.setravel.swifttravel.mapper.SupervisorTrainMapper;
import com.setravel.swifttravel.mapper.TrainNumberMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SupervisorTrainServiceImpl implements SupervisorTrainService {

    @Resource
    private SupervisorTrainMapper supervisorTrainMapper;

    @Resource
    private TrainNumberMapper trainNumberMapper;

    @Resource
    private CarriagesMapper carriagesMapper;

    @Resource
    private SeatsMapper seatsMapper;

    @Override
    public Result addTrainNumber(TrainNumberDetail trainnumbers_detail) {
        Result result = check(trainnumbers_detail);
        if (result.isError())
            return result;

        List<City> cityList = getCityList(trainnumbers_detail);

        Trainnumbers trainnumbers = convertToTrainNumber(trainnumbers_detail, cityList);
        trainNumberMapper.insert(trainnumbers);

        carriagesMapper.insert(convertToCarriages(trainnumbers_detail, cityList, trainnumbers));

        seatsMapper.insert(convertToSeats(trainnumbers_detail, cityList, trainnumbers));

        return Result.success();
    }

    private List<Seats> convertToSeats(TrainNumberDetail trainnumbers_detail, List<City> cityList, Trainnumbers trainnumbers) {
        List<Seats> seats = new ArrayList<>();
        byte[] flag = new byte[8];
        Arrays.fill(flag, (byte) 0xFF);
        for (int i = 0; i < trainnumbers_detail.getBusiness_coach().size(); i++) {
            for (int j = 0; j < trainnumbers_detail.getBusiness_seats_num().get(i); j++) {
                Seats seat = new Seats()
                        .setId(UUID.randomUUID().toString().getBytes())
                        .setTrainNumber(trainnumbers.getId())
                        .setCoach(trainnumbers_detail.getBusiness_coach().get(i))
                        .setSeatType("Business")
                        .setFlags(flag);
                seats.add(seat);
            }
        }

        for (int i = 0; i < trainnumbers_detail.getFirst_coach().size(); i++) {
            for (int j = 0; j < trainnumbers_detail.getFirst_seats_num().get(i); j++) {
                Seats seat = new Seats()
                        .setId(UUID.randomUUID().toString().getBytes())
                        .setTrainNumber(trainnumbers.getId())
                        .setCoach(trainnumbers_detail.getFirst_coach().get(i))
                        .setSeatType("First")
                        .setFlags(flag);
                seats.add(seat);
            }
        }

        for (int i = 0; i < trainnumbers_detail.getSecond_coach().size(); i++) {
            for (int j = 0; j < trainnumbers_detail.getSecond_seats_num().get(i); j++) {
                Seats seat = new Seats()
                        .setId(UUID.randomUUID().toString().getBytes())
                        .setTrainNumber(trainnumbers.getId())
                        .setCoach(trainnumbers_detail.getSecond_coach().get(i))
                        .setSeatType("Second")
                        .setFlags(flag);
                seats.add(seat);
            }
        }


        for (int i = 0; i < trainnumbers_detail.getNo_seats_num(); i++) {
            Seats seat = new Seats()
                    .setId(UUID.randomUUID().toString().getBytes())
                    .setTrainNumber(trainnumbers.getId())
                    .setSeatType("NoSeat")
                    .setFlags(flag);
            seats.add(seat);
        }
        return seats;
    }

    private List<City> getCityList(TrainNumberDetail trainnumbers_detail) {
        List<City> cityList = new ArrayList<>();
        cityList = supervisorTrainMapper
                .selectCitiesByStations(trainnumbers_detail.getStationLine());
        Map<String, City> cityMap = cityList.stream()
                .collect(Collectors.toMap(City::getCityName, Function.identity()));
        cityList = trainnumbers_detail.getStationLine().stream()
                .map(cityMap::get)
                .toList();
        return cityList;
    }

    private List<Carriages> convertToCarriages(TrainNumberDetail trainnumbers_detail, List<City> cityList, Trainnumbers trainnumbers) {
        int business_seats = trainnumbers_detail.getBusiness_seats_num().stream().reduce(0, Integer::sum);
        int first_seats = trainnumbers_detail.getFirst_seats_num().stream().reduce(0, Integer::sum);
        int second_seats = trainnumbers_detail.getSecond_seats_num().stream().reduce(0, Integer::sum);
        int all_seats = business_seats + first_seats + second_seats;

        List<Carriages> carriages = new ArrayList<>();
        for (int i = 0; i < trainnumbers_detail.getStationLine().size() - 1; i++) {
            for (int j = i + 1; j < trainnumbers_detail.getStationLine().size(); j++) {
                Carriages carriage = new Carriages()
                        .setId(UUID.randomUUID().toString().getBytes())
                        .setTrainNumber(trainnumbers.getId())
                        .setDepCity(cityList.get(i).getCityName())
                        .setArrCity(cityList.get(j).getCityName())
                        .setDepStation(trainnumbers_detail.getStationLine().get(i))
                        .setArrStation(trainnumbers_detail.getStationLine().get(j))
                        .setArrTime(trainnumbers_detail.getTimeLine().get(j))
                        .setWaitTime(trainnumbers_detail.getWaitingTimeLine().get(j))
                        .setAllNumber(all_seats)
                        .setFirstNumber(first_seats)
                        .setSecondNumber(second_seats)
                        .setBusinessNumber(business_seats)
                        .setNoSeatNumber(trainnumbers_detail.getNo_seats_num())
                        .setBusinessPrice(trainnumbers_detail.getBusiness_price().stream().skip(i).limit(j - i).reduce(BigDecimal.ZERO, BigDecimal::add))
                        .setFirstPrice(trainnumbers_detail.getFirst_price().stream().skip(i).limit(j - i).reduce(BigDecimal.ZERO, BigDecimal::add))
                        .setSecondPrice(trainnumbers_detail.getSecond_price().stream().skip(j - i).reduce(BigDecimal.ZERO, BigDecimal::add))
                        .setNoSeatPrice(trainnumbers_detail.getNo_seat_price().stream().skip(j - i).reduce(BigDecimal.ZERO, BigDecimal::add));

                carriages.add(carriage);
            }
        }
        return carriages;
    }

    private Trainnumbers convertToTrainNumber(TrainNumberDetail trainnumbers_detail, List<City> cityList) {
        return new Trainnumbers().setId(UUID.randomUUID().toString().getBytes())
                .setDepCity(cityList.getFirst().getCityName())
                .setArrCity(cityList.getLast().getCityName())
                .setTrainNumber(trainnumbers_detail.getTrainNumber())
                .setDepStation(trainnumbers_detail.getFirstStation())
                .setArrStation(trainnumbers_detail.getLastStation())
                .setDepTime(trainnumbers_detail.getTimeLine().getFirst())
                .setArrTime(trainnumbers_detail.getTimeLine().getLast());

    }

    private Result check(TrainNumberDetail trainnumbers_detail) {
        if (trainnumbers_detail == null)
            return Result.error("车次信息不能为空");
        if (trainnumbers_detail.getTrainNumber() == null)
            return Result.error("车次号不能为空");

        if (!checkConsistency(List.of(
                trainnumbers_detail.getStationLine().size(),
                trainnumbers_detail.getTimeLine().size(),
                trainnumbers_detail.getWaitingTimeLine().size(),
                trainnumbers_detail.getBusiness_price().size(),
                trainnumbers_detail.getFirst_price().size(),
                trainnumbers_detail.getSecond_price().size(),
                trainnumbers_detail.getNo_seat_price().size()
        ))) return Result.error("The Line length is not same");

        if (trainnumbers_detail.getBusiness_coach().size() != trainnumbers_detail.getBusiness_seats_num().size())
            return  Result.error("Business coach length is not same");

        if (trainnumbers_detail.getFirst_coach().size() != trainnumbers_detail.getFirst_seats_num().size())
            return  Result.error("First coach length is not same");

        if (trainnumbers_detail.getSecond_coach().size() != trainnumbers_detail.getSecond_seats_num().size())
            return  Result.error("Second coach length is not same");

        return Result.success();
    }

    // 提取的检查方法
    private static <T> boolean checkConsistency(List<T> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        T first = list.getFirst();
        return list.stream().allMatch(e ->
                Objects.equals(first, e)
        );
    }
}
