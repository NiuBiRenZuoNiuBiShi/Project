package com.setravel.swifttravel.controller;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.request.BuyTicketsRequest;
import com.setravel.swifttravel.exception.SeatVersionException;
import com.setravel.swifttravel.service.BuyTicketsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class BuyTrainTicketsController {

    @Resource
    private BuyTicketsService buyTicketsService;

    @PostMapping("/api/buyTickets")
    public Result buyTickets(@RequestBody BuyTicketsRequest request) {
        try {
            return buyTicketsService.buyTickets(request);
        } catch (SeatVersionException e) {
            return Result.error("Seat version mismatch: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return Result.error("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return Result.error("An error occurred: " + e.getMessage());
        }
    }
}
