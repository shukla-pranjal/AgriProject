/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.agriproject.service;

import java.util.List;

import com.agriproject.dto.OrderItemDTO;

public interface  OrderItemService {

    List<OrderItemDTO> getAllOrderItems();

    OrderItemDTO getOrderItemById(Long id) throws Exception;

    OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO);

    OrderItemDTO updateOrderItem(Long id, OrderItemDTO orderItemDTO)throws Exception;

    void deleteOrderItem(Long id) throws Exception;

}
