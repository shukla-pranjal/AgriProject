package com.agriproject.controller;

import com.agriproject.dto.OrderItemDTO;
import com.agriproject.service.OrderItemService;
import com.agriproject.util.CommonUtil;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/order-items")
public class OrderItemController {


   
    private final OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<OrderItemDTO> orderItems = orderItemService.getAllOrderItems();
        return CommonUtil.createBuildResponse(orderItems, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws Exception {
        OrderItemDTO orderItem = orderItemService.getOrderItemById(id);
        return CommonUtil.createBuildResponse(orderItem, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderItemDTO orderItemDTO) {
        OrderItemDTO created = orderItemService.createOrderItem(orderItemDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody OrderItemDTO orderItemDTO)throws Exception {
        OrderItemDTO updated = orderItemService.updateOrderItem(id, orderItemDTO);
        return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception{
        orderItemService.deleteOrderItem(id);
        return CommonUtil.createBuildResponseMessage("Order item deleted successfully", HttpStatus.OK);
    }
}
