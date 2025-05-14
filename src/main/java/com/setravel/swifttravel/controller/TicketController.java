package com.setravel.swifttravel.controller;

import com.setravel.swifttravel.entities.*;
import com.setravel.swifttravel.service.impl.CarriagesService;
import com.setravel.swifttravel.service.impl.TicketOrdersService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Tengpaz
 */
@RestController
@RequestMapping("api/ticket")
public class TicketController {

    @Resource
    private CarriagesService carriagesService;
    @Autowired
    private TicketOrdersService ticketOrdersService;

    /**
     * 查询直达车次
     * @param depStation 出发站
     * @param depCity 出发城市
     * @param arrStation 到达站
     * @param arrCity 到达城市
     * @param depDate 出发日期
     * @return 车厢信息列表
     */
    @GetMapping("/getCarriages")
    public Result getCarriages(@RequestParam String depStation, @RequestParam String depCity,
                               @RequestParam String arrStation, @RequestParam String arrCity,
                               @RequestParam String depDate) {
        List<CarriageThrough> carriageThrough = carriagesService.getCarriageThrough(depStation, depCity, arrStation, arrCity, depDate);
        return new Result(200, "成功获取到直达车次。", carriageThrough);
    }

    /**
     * 查询中转车次
     * @param depCity 出发城市
     * @param arrCity 到达城市
     * @param depDate 出发日期
     * @return 车厢信息列表
     */
    @GetMapping("/getCarriagesTransfer")
    public Result getCarriagesTransfer(@RequestParam String depCity,
                               @RequestParam String arrCity,
                               @RequestParam String depDate) {
        List<CarriageTransfer> carriageTransfer = carriagesService.getCarriageTransfer(depCity, arrCity, depDate);
        return new Result(200, "成功获取到中转车次。", carriageTransfer);
    }

    /**
     * 查询特定类型车次
     * @param depStation 出发站
     * @param depCity 出发城市
     * @param arrStation 到达站
     * @param arrCity 到达城市
     * @param depDate 出发日期
     * @param type 车厢类型
     * @return 车厢信息列表
     */
    @GetMapping("/getCarriagesByType")
    public Result getCarriagesByType(@RequestParam String depStation, @RequestParam String depCity,
                                        @RequestParam String arrStation, @RequestParam String arrCity,
                                        @RequestParam String depDate, @RequestParam char type) {
        return new Result(200, "成功获取到" + type + "类型车", carriagesService.getCarriageType(type, carriagesService.getCarriages(depStation, depCity, arrStation, arrCity, depDate)));
    }

    /**
     * 查询用户历史订单
     * @param userId 用户ID
     * @return 购票信息列表
     */
    @GetMapping("/getTicketOrders")
    public Result getTicketOrders(@RequestParam String userId) {
        return new Result(200, "成功获取到用户订单", ticketOrdersService.getTicketOrders(userId));
    }

    /**
     * 删除订单
     * @param orderId 订单ID
     * @return 删除结果
     */
    @DeleteMapping("/deleteTicketOrder")
    public Result deleteTicketOrder(@RequestParam String orderId) {
        return new Result(200, "成功删除订单", ticketOrdersService.deleteTicketOrder(orderId));
    }
}
