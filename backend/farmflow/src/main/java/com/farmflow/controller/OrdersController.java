package com.farmflow.controller;

import com.farmflow.dto.OrdersDTO;
import com.farmflow.dto.PaginatedResponse;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.endpoint.OrdersEndpoint;
import com.farmflow.enums.OrdersStatus;
import com.farmflow.service.OrdersService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrdersController implements OrdersEndpoint {

    private final OrdersService ordersService;

    @Override
    public ResponseEntity<?> placeOrderFromCart(@PathVariable Integer cartId) throws Exception {
        OrdersDTO ordersDTO = ordersService.placeOrderFromCart(cartId);
        return CommonUtil.createBuildResponse(ordersDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> createOrder(@RequestBody OrdersDTO ordersDTO) throws Exception {
        OrdersDTO createdOrder = ordersService.createManualOrder(ordersDTO);
        return CommonUtil.createBuildResponse(createdOrder, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> reorder(@PathVariable Integer id) throws Exception {
        OrdersDTO reordered = ordersService.reorder(id);
        return CommonUtil.createBuildResponse(reordered, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> cancelOrder(@PathVariable Integer id) throws Exception {
        OrdersDTO cancelledOrder = ordersService.cancelOrder(id);
        return CommonUtil.createBuildResponse(cancelledOrder, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer id, @RequestParam OrdersStatus status) throws Exception {
        OrdersDTO updatedOrder = ordersService.updateOrderStatus(id, status);
        return CommonUtil.createBuildResponse(updatedOrder, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getOrderById(@PathVariable Integer id) throws Exception {
        OrdersDTO ordersDTO = ordersService.getOrderById(id);
        return CommonUtil.createBuildResponse(ordersDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getOrdersByUser(@PathVariable Integer userId) throws Exception {
        List<OrdersDTO> ordersDTOList = ordersService.getOrdersByUser(userId);
        return CommonUtil.createBuildResponse(ordersDTOList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllOrders() {
        List<OrdersDTO> orders = ordersService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<?> deleteOrder(@PathVariable Integer id) throws Exception {
        ordersService.deleteOrder(id);
        return CommonUtil.createBuildResponseMessage("Orders deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchOrders(Integer userId, LocalDateTime fromDate, LocalDateTime toDate, OrdersStatus status, Double minTotalPrice, Double maxTotalPrice) {
        List<OrdersDTO> results = ordersService.searchOrders(userId, fromDate, toDate, status, minTotalPrice, maxTotalPrice);
        return CommonUtil.createBuildResponse(results, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllOrdersPaged(PaginationRequest paginationRequest) {
        Page<OrdersDTO> page = ordersService.getAllOrdersPaged(paginationRequest);
        PaginatedResponse<OrdersDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getOrdersByUserPaged(Integer userId, PaginationRequest paginationRequest) throws Exception {
        Page<OrdersDTO> page = ordersService.getOrdersByUserPaged(userId, paginationRequest);
        PaginatedResponse<OrdersDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchOrdersPaged(PaginationRequest paginationRequest, Integer userId, LocalDateTime fromDate, LocalDateTime toDate, OrdersStatus status, Double minTotalPrice, Double maxTotalPrice) {
        Page<OrdersDTO> page = ordersService.searchOrdersPaged(paginationRequest, userId, fromDate, toDate, status, minTotalPrice, maxTotalPrice);
        PaginatedResponse<OrdersDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }
}