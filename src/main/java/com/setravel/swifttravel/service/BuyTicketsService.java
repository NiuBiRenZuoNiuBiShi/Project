package com.setravel.swifttravel.service;

import com.setravel.swifttravel.entities.Result;

public interface BuyTicketsService {
    Result buyTickets(Integer carriageId, String seatType, Integer seatNum);
}
