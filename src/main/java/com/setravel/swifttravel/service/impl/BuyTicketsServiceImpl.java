package com.setravel.swifttravel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.setravel.swifttravel.entities.Carriages;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.Seats;
import com.setravel.swifttravel.mapper.CarriagesMapper;
import com.setravel.swifttravel.mapper.SeatsMapper;
import com.setravel.swifttravel.mapper.TrainNumberMapper;
import com.setravel.swifttravel.service.BuyTicketsService;
import com.setravel.swifttravel.utils.BitMapUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BuyTicketsServiceImpl implements BuyTicketsService {

    @Resource
    private SeatsMapper seatsMapper;

    @Resource
    private TrainNumberMapper trainNumberMapper;

    @Resource
    private CarriagesMapper carriagesMapper;

    @Override
    public Result buyTickets(Integer carriageId, String seatType, Integer seatNum) {
        Carriages carriage = carriagesMapper.selectById(carriageId);
        synchronized (BuyTicketsServiceImpl.class) {
            Seats seat = seatsMapper.selectOne(new QueryWrapper<Seats>().lambda()
                .eq(Seats::getSeatType, seatType)
                .eq(Seats::getTrainNumber, carriage.getTrainNumber())
                .last("limit 1"));
            if (seat == null) {
                return Result.error("Can not Find Tickets");
            }
            byte[] newFlag = BitMapUtil.and(seat.getFlags(), BitMapUtil.not(carriage.getFlag()));
            seat.setFlags(newFlag);
            switch (seatType) {
                case "First": {
                    carriage.setBusinessNumber(carriage.getBusinessNumber() - 1);
                }
                case "Second": {
                    carriage.setBusinessNumber(carriage.getBusinessNumber() - 1);
                }
                case "Business": {
                    carriage.setBusinessNumber(carriage.getBusinessNumber() - 1);
                }
                case "NoSeat": {
                    carriage.setBusinessNumber(carriage.getNoSeatNumber() - 1);
                }
            }
            seatsMapper.deleteById(seat.getId());
            seatsMapper.insert(seat);
        }
        return Result.success();
    }
}
