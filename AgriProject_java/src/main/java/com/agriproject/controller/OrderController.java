package com.agriproject.controller;

import com.agriproject.dto.OrderDTO;
import com.agriproject.service.OrderService;
import com.agriproject.util.CommonUtil;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return CommonUtil.createBuildResponse(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws Exception{
        OrderDTO order = orderService.getOrderById(id);
        return CommonUtil.createBuildResponse(order, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderDTO orderDTO) {
        OrderDTO created = orderService.createOrder(orderDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody OrderDTO orderDTO)throws Exception {
        OrderDTO updated = orderService.updateOrder(id, orderDTO);
        return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id)throws Exception {
        orderService.deleteOrder(id);
        return CommonUtil.createBuildResponseMessage("Order deleted successfully", HttpStatus.OK);
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrdersByUser(@PathVariable Long userId)throws Exception{
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
        return CommonUtil.createBuildResponse(orders, HttpStatus.OK);
    }


}
