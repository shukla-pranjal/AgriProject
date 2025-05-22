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
    private static final String CLASS_NAME = OrdersController.class.getSimpleName();

    @Override
    public ResponseEntity<?> placeOrderFromCart(@PathVariable Integer cartId) throws Exception {
        String methodName = "placeOrderFromCart";
        log.debug("{} : {} :: Started with cartId: {}", CLASS_NAME, methodName, cartId);
        try {
            OrdersDTO ordersDTO = ordersService.placeOrderFromCart(cartId);
            log.info("{} : {} :: Successfully placed order from cart for cartId: {}", CLASS_NAME, methodName, cartId);
            return CommonUtil.createBuildResponse(ordersDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to place order from cart for cartId: {}, error: {}", CLASS_NAME, methodName, cartId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> createOrder(@RequestBody OrdersDTO ordersDTO) throws Exception {
        String methodName = "createOrder";
        log.debug("{} : {} :: Started with ordersDTO: {}", CLASS_NAME, methodName, ordersDTO);
        try {
            OrdersDTO createdOrder = ordersService.createManualOrder(ordersDTO);
            log.info("{} : {} :: Successfully created order", CLASS_NAME, methodName);
            return CommonUtil.createBuildResponse(createdOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to create order, error: {}", CLASS_NAME, methodName, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> reorder(@PathVariable Integer id) throws Exception {
        String methodName = "reorder";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            OrdersDTO reordered = ordersService.reorder(id);
            log.info("{} : {} :: Successfully reordered for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(reordered, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to reorder for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> cancelOrder(@PathVariable Integer id) throws Exception {
        String methodName = "cancelOrder";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            OrdersDTO cancelledOrder = ordersService.cancelOrder(id);
            log.info("{} : {} :: Successfully cancelled order for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(cancelledOrder, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to cancel order for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer id, @RequestParam OrdersStatus status) throws Exception {
        String methodName = "updateOrderStatus";
        log.debug("{} : {} :: Started with id: {}, status: {}", CLASS_NAME, methodName, id, status);
        try {
            OrdersDTO updatedOrder = ordersService.updateOrderStatus(id, status);
            log.info("{} : {} :: Successfully updated order status for id: {}, new status: {}", CLASS_NAME, methodName, id, status);
            return CommonUtil.createBuildResponse(updatedOrder, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to update order status for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getOrderById(@PathVariable Integer id) throws Exception {
        String methodName = "getOrderById";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            OrdersDTO ordersDTO = ordersService.getOrderById(id);
            log.info("{} : {} :: Successfully retrieved order for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(ordersDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve order for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getOrdersByUser(@PathVariable Integer userId) throws Exception {
        String methodName = "getOrdersByUser";
        log.debug("{} : {} :: Started with userId: {}", CLASS_NAME, methodName, userId);
        try {
            List<OrdersDTO> ordersDTOList = ordersService.getOrdersByUser(userId);
            log.info("{} : {} :: Successfully retrieved {} orders for userId: {}", CLASS_NAME, methodName, ordersDTOList.size(), userId);
            return CommonUtil.createBuildResponse(ordersDTOList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve orders for userId: {}, error: {}", CLASS_NAME, methodName, userId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getAllOrders() {
        String methodName = "getAllOrders";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);
        List<OrdersDTO> orders = ordersService.getAllOrders();
        log.info("{} : {} :: Successfully retrieved {} orders", CLASS_NAME, methodName, orders.size());
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<?> deleteOrder(@PathVariable Integer id) throws Exception {
        String methodName = "deleteOrder";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            ordersService.deleteOrder(id);
            log.info("{} : {} :: Successfully deleted order for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponseMessage("Orders deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to delete order for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchOrders(Integer userId, LocalDateTime fromDate, LocalDateTime toDate, OrdersStatus status, Double minTotalPrice, Double maxTotalPrice) {
        String methodName = "searchOrders";
        log.debug("{} : {} :: Started with userId: {}, fromDate: {}, toDate: {}, status: {}, minTotalPrice: {}, maxTotalPrice: {}", CLASS_NAME, methodName, userId, fromDate, toDate, status, minTotalPrice, maxTotalPrice);
        List<OrdersDTO> results = ordersService.searchOrders(userId, fromDate, toDate, status, minTotalPrice, maxTotalPrice);
        log.info("{} : {} :: Successfully retrieved {} orders", CLASS_NAME, methodName, results.size());
        return CommonUtil.createBuildResponse(results, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllOrdersPaged(PaginationRequest paginationRequest) {
        String methodName = "getAllOrdersPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}", CLASS_NAME, methodName, paginationRequest);
        Page<OrdersDTO> page = ordersService.getAllOrdersPaged(paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged orders, page: {}, size: {}", CLASS_NAME, methodName, page.getNumber(), page.getSize());
        PaginatedResponse<OrdersDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getOrdersByUserPaged(Integer userId, PaginationRequest paginationRequest) throws Exception {
        String methodName = "getOrdersByUserPaged";
        log.debug("{} : {} :: Started with userId: {}, paginationRequest: {}", CLASS_NAME, methodName, userId, paginationRequest);
        try {
            Page<OrdersDTO> page = ordersService.getOrdersByUserPaged(userId, paginationRequest);
            log.info("{} : {} :: Successfully retrieved paged orders for userId: {}, page: {}, size: {}", CLASS_NAME, methodName, userId, page.getNumber(), page.getSize());
            PaginatedResponse<OrdersDTO> response = PaginatedResponse.fromPage(page);
            return CommonUtil.createBuildResponse(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve paged orders for userId: {}, error: {}", CLASS_NAME, methodName, userId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchOrdersPaged(PaginationRequest paginationRequest, Integer userId, LocalDateTime fromDate, LocalDateTime toDate, OrdersStatus status, Double minTotalPrice, Double maxTotalPrice) {
        String methodName = "searchOrdersPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}, userId: {}, fromDate: {}, toDate: {}, status: {}, minTotalPrice: {}, maxTotalPrice: {}", CLASS_NAME, methodName, paginationRequest, userId, fromDate, toDate, status, minTotalPrice, maxTotalPrice);
        Page<OrdersDTO> page = ordersService.searchOrdersPaged(paginationRequest, userId, fromDate, toDate, status, minTotalPrice, maxTotalPrice);
        log.info("{} : {} :: Successfully retrieved paged orders, page: {}, size: {}", CLASS_NAME, methodName, page.getNumber(), page.getSize());
        PaginatedResponse<OrdersDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }
}