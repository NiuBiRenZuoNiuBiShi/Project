package com.setravel.swifttravel.task;

import com.setravel.swifttravel.entities.TicketsOrders;
import com.setravel.swifttravel.entities.Trainnumbers;
import com.setravel.swifttravel.mapper.TicketOrdersMapper;
import com.setravel.swifttravel.mapper.TrainNumberMapper;
import com.setravel.swifttravel.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class TrainDepartureReminderTask {
    @Autowired
    private TicketOrdersMapper ticketOrdersMapper;
    @Autowired
    private TrainNumberMapper trainNumberMapper;
    @Autowired
    private NotificationService notificationService;

    /**
     * 每10分钟检查一次是否有3小时后即将发车的车次
     */
    @Scheduled(cron = "0 */10 * * * ?")  // 每10分钟执行一次
    public void sendDepartureReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusHours(3);

        List<TicketsOrders> orders = ticketOrdersMapper.selectPendingDepartureWithin(targetTime);
        for (TicketsOrders order : orders) {
            Trainnumbers train = trainNumberMapper.selectById(order.getTrainId());
            notificationService.sendTrainDepartureReminder(order, train);
            log.info("已发送发车提醒通知，订单ID: {}", order.getId());
        }
    }
}
