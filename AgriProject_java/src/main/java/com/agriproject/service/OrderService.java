/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.agriproject.service;

import java.util.List;

import com.agriproject.dto.OrderDTO;

public interface  OrderService {

    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(Long id) throws Exception;

    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO updateOrder(Long id, OrderDTO orderDTO) throws Exception;

    void deleteOrder(Long id) throws Exception;

    // List<OrderDTO> getOrdersByStatus(String status); // TODO 

    List<OrderDTO> getOrdersByUserId(Long userId) throws Exception;

}
