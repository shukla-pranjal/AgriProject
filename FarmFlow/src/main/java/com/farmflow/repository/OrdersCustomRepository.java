package com.farmflow.repository;

import com.farmflow.entity.Orders;
import com.farmflow.enums.OrdersStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersCustomRepository {
    List<Orders> searchOrders(Integer userId, LocalDateTime fromDate, LocalDateTime toDate,
                              OrdersStatus status, Double minTotalPrice, Double maxTotalPrice);
}
