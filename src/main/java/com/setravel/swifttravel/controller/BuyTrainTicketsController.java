package com.setravel.swifttravel.controller;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.service.BuyTicketsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BuyTrainTicketsController {

    @Resource
    private BuyTicketsService buyTicketsService;

    @PostMapping("/api/buyTickets")
    public Result buyTrainTickets (@RequestParam("carriageID") Integer carriageId
        , @RequestParam("seatType") String seatType
        , @RequestParam("seatNum") Integer seatNum) {
        return buyTicketsService.buyTickets(carriageId, seatType, seatNum);
    }
}
