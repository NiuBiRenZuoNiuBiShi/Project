package com.setravel.swifttravel.service;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.TicketsOrders;
import com.setravel.swifttravel.service.impl.TicketOrdersService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

public interface BuyTicketsService {

    Result buyTickets(Integer carriageID, String seatType, Integer seatID, Integer contactID);
}
