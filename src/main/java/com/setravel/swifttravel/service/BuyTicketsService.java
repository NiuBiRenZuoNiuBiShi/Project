package com.setravel.swifttravel.service;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.request.BuyTicketsRequest;
import com.setravel.swifttravel.exception.SeatVersionException;

public interface BuyTicketsService {
    Result buyTickets(BuyTicketsRequest request) throws SeatVersionException;
}
