package com.farmflow.scheduler;

import com.farmflow.entity.Orders;
import com.farmflow.enums.OrdersStatus;
import com.farmflow.repository.OrdersRepository;
import com.farmflow.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersScheduler {

    private final OrdersRepository orderRepository;
    private final OrdersService ordersService;

    // Runs every day at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void cancelPendingOrdersOlderThan10Days() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(10);
        List<Orders> expiredOrders = orderRepository.findByStatusAndOrderDateBefore(OrdersStatus.PENDING, threshold);
        log.info("Found {} pending orders older than 10 days to cancel", expiredOrders.size());

        expiredOrders.forEach(order -> {
            try {
                ordersService.cancelOrder(order.getId());
                log.info("Cancelled pending order[id={}]", order.getId());
            } catch (Exception e) {
                log.error("Failed to cancel order[id={}]", order.getId(), e);
            }
        });
    }
}
