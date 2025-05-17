package com.setravel.swifttravel.service;

import com.setravel.swifttravel.entities.TicketsOrders;
import com.setravel.swifttravel.entities.Trainnumbers;

public interface NotificationService {
    void sendTicketOrderPaySuccessMessage(TicketsOrders ticketsOrder);

    void sendTicketOrderCancelMessage(TicketsOrders ticketsOrder);

    void sendTrainDepartureReminder(TicketsOrders ticketsOrder, Trainnumbers train);
}
