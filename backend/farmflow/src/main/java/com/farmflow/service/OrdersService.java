package com.farmflow.service;

import com.farmflow.dto.OrdersDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.enums.OrdersStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersService {

    OrdersDTO placeOrderFromCart(Integer cartId) throws Exception;

    OrdersDTO getOrderById(Integer id) throws Exception;

    List<OrdersDTO> getOrdersByUser(Integer userId)throws Exception ;

    List<OrdersDTO> getAllOrders();

    OrdersDTO cancelOrder(Integer orderId) throws Exception;

    OrdersDTO updateOrderStatus(Integer orderId, OrdersStatus status) throws Exception;

    void deleteOrder(Integer orderId) throws Exception;

    OrdersDTO createManualOrder(OrdersDTO ordersDTO) throws Exception;

    OrdersDTO reorder(Integer id) throws Exception;

    List<OrdersDTO> searchOrders(Integer userId, LocalDateTime fromDate, LocalDateTime toDate, OrdersStatus status, Double minTotalPrice, Double maxTotalPrice);

    Page<OrdersDTO> getAllOrdersPaged(PaginationRequest paginationRequest);

    Page<OrdersDTO> getOrdersByUserPaged(Integer userId, PaginationRequest paginationRequest)throws Exception;

    Page<OrdersDTO> searchOrdersPaged(PaginationRequest paginationRequest, Integer userId, LocalDateTime fromDate, LocalDateTime toDate, OrdersStatus status, Double minTotalPrice, Double maxTotalPrice);
}
