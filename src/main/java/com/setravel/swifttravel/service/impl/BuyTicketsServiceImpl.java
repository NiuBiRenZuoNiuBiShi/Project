package com.setravel.swifttravel.service.impl;

import com.setravel.swifttravel.entities.Carriages;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.Seats;
import com.setravel.swifttravel.entities.output.BuyTicketsOutput;
import com.setravel.swifttravel.entities.request.BuyTicketsRequest;
import com.setravel.swifttravel.exception.SeatVersionException;
import com.setravel.swifttravel.mapper.CarriagesMapper;
import com.setravel.swifttravel.mapper.SeatsMapper;
import com.setravel.swifttravel.mapper.TrainNumberMapper;
import com.setravel.swifttravel.service.BuyTicketsService;
import jakarta.annotation.Resource;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuyTicketsServiceImpl implements BuyTicketsService {

    @Resource
    private SeatsMapper seatsMapper;

    @Resource
    private TrainNumberMapper trainNumberMapper;

    @Resource
    private CarriagesMapper carriagesMapper;

    @Transactional
    @Override
    public Result buyTickets(BuyTicketsRequest request) throws SeatVersionException {
        List<byte[]> seatIdList = request.getSeatIdList().stream().map(Base64.getDecoder()::decode)
                .collect(Collectors.toList());

        byte[] carriageId = Base64.getDecoder().decode(request.getCarriageId());

        // 加载并检查车厢
        Carriages carriages = carriagesMapper.selectById(carriageId);
        if (carriages == null) {
            throw new IllegalArgumentException("Carriage not found");
        }

        // 加载座位
        List<Seats> seats = seatsMapper.selectByIds(seatIdList);

        for (int i = 0; i < seats.size(); i++) {
            Seats seat = seats.get(i);
            if (seat == null) {
                throw new IllegalArgumentException("Seat not found");
            }
            if (!carriageId.equals(seat.getTrainNumber())) {
                throw new IllegalArgumentException("Seat does not belong to the specified carriage");
            }
            if (seat.getVersion() != request.getVersionList().get(i)) {
                throw new SeatVersionException("Seat version mismatch");
            }
        }

        BigDecimal totalMoney = BigDecimal.ZERO;

        for (Seats seat : seats) {
            switch (seat.getSeatType()) {
            case "Business":
                carriages.setBusinessNumber(carriages.getBusinessNumber() - 1);
                totalMoney = totalMoney.add(carriages.getBusinessPrice());
                break;
            case "First":
                carriages.setFirstNumber(carriages.getFirstNumber() - 1);
                totalMoney = totalMoney.add(carriages.getFirstPrice());
                break;
            case "Second":
                carriages.setSecondNumber(carriages.getSecondNumber() - 1);
                totalMoney = totalMoney.add(carriages.getSecondPrice());
                break;
            case "NoSeat":
                carriages.setNoSeatNumber(carriages.getNoSeatNumber() - 1);
                totalMoney = totalMoney.add(carriages.getNoSeatPrice());
                break;
            }

            // 乐观锁更新座位
            int updated = seatsMapper.updateById(seat);
            if (updated == 0) {
                throw new SeatVersionException("Seat update failed due to concurrent modification.");
            }
        }

        // 乐观锁更新车厢
        int carriageUpdated = carriagesMapper.updateById(carriages);
        if (carriageUpdated == 0) {
            throw new SeatVersionException("Carriage update failed due to concurrent modification.");
        }

        // 这里你可以生成订单、扣款等逻辑

        return Result.success(
                new BuyTicketsOutput().setCarriageId(request.getCarriageId()).setSeatId(request.getSeatIdList())
                        .setTrainNumber(trainNumberMapper.selectById(carriageId).getTrainNumber())
                        .setMessage("Purchase successful, total amount: " + totalMoney)
                        .setContactIdList(request.getContactIdList()));
    }

}
