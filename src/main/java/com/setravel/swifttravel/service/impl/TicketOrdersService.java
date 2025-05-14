package com.setravel.swifttravel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.setravel.swifttravel.entities.Carriages;
import com.setravel.swifttravel.entities.Seats;
import com.setravel.swifttravel.entities.TicketsOrders;
import com.setravel.swifttravel.mapper.SeatsMapper;
import com.setravel.swifttravel.mapper.TicketOrdersMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketOrdersService {

    @Resource
    private TicketOrdersMapper ticketOrdersMapper;
    @Autowired
    private SeatsMapper seatsMapper;


    public List<TicketsOrders> getTicketOrders(String userId) {
        QueryWrapper<TicketsOrders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("del", false) // 查询未删除的记录
                .orderByAsc("dep_date"); // 按照出发时间升序排列

        return ticketOrdersMapper.selectList(queryWrapper);
    }

    /**
     * 根据订单ID删除相应订单（数据同步Seats Carriages）
     * @param orderId
     * @return
     */
    public Object deleteTicketOrder(String orderId) {
        QueryWrapper<TicketsOrders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId)
                .eq("del", false); // 查询未删除的记录
        TicketsOrders ticketOrder = ticketOrdersMapper.selectOne(queryWrapper);
        if (ticketOrder != null) {
            ticketOrder.setDel(true);
            ticketOrdersMapper.updateById(ticketOrder);
            // 更新对应车次的座位状态
            // 首先通过订单查询到对应的车次ID
            String trainId = new String(ticketOrder.getTrainId()); // debug
            // 然后通过车次ID查询到对应的车厢信息
            QueryWrapper<Carriages> carriagesQueryWrapper = new QueryWrapper<>();
            carriagesQueryWrapper.eq("train_id", trainId)
                    .eq("del", false); // 查询未删除的记录
            CarriagesService carriagesService = new CarriagesService();
            Carriages carriage = carriagesService.getCarriageByTrainId(trainId);
            if (carriage != null) {
                carriage.setAllNumber(carriage.getAllNumber() + 1);
                // 获取座位类型
                QueryWrapper<Seats> seatsQueryWrapper = new QueryWrapper<>();
                seatsQueryWrapper.eq("id", ticketOrder.getSeatId())
                        .eq("del", false); // 查询未删除的记录
                Seats seat = seatsMapper.selectOne(seatsQueryWrapper); // debug
                if (seat != null) {
                    // 更新座位状态
                    seat.setFlags(new byte[]{0}); // 0表示可用
                    seatsMapper.updateById(seat);
                    // 更新车厢信息
                    String seatType = seat.getSeatType();
                    if (seatType != null) {
                        switch (seatType) {
                            case "一等座":
                                carriage.setFirstNumber(carriage.getFirstNumber() + 1);
                                break;
                            case "二等座":
                                carriage.setSecondNumber(carriage.getSecondNumber() + 1);
                                break;
                            case "商务座":
                                carriage.setBusinessNumber(carriage.getBusinessNumber() + 1);
                                break;
                            case "无座":
                                carriage.setNoSeatNumber(carriage.getNoSeatNumber() + 1);
                                break;
                        }
                    }
                }
                // 更新车厢信息
                carriagesService.updateCarriage(carriage);
            }
            return ticketOrder;
        } else {
            return null;
        }
    }
}
