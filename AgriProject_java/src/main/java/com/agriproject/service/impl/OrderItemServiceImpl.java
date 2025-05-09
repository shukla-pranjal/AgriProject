package com.agriproject.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.agriproject.dto.OrderItemDTO;
import com.agriproject.enitity.OrderItem;
import com.agriproject.exception.ResourceNotFoundException;
import com.agriproject.repository.OrderItemRepository;
import com.agriproject.service.OrderItemService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<OrderItemDTO> getAllOrderItems() {
        return orderItemRepository.findAll().stream()
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDTO getOrderItemById(Long id)throws Exception {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with ID: " + id));
        return modelMapper.map(orderItem, OrderItemDTO.class);
    }

    @Override
    public OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = modelMapper.map(orderItemDTO, OrderItem.class);
        OrderItem saved = orderItemRepository.save(orderItem);
        return modelMapper.map(saved, OrderItemDTO.class);
    }

    @Override
    public OrderItemDTO updateOrderItem(Long id, OrderItemDTO orderItemDTO) throws Exception {
        OrderItem existing = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with ID: " + id));

        modelMapper.map(orderItemDTO, existing);
        OrderItem updated = orderItemRepository.save(existing);
        return modelMapper.map(updated, OrderItemDTO.class);
    }

    @Override
    public void deleteOrderItem(Long id) throws Exception{
        OrderItem existing = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with ID: " + id));
        orderItemRepository.delete(existing);
    }
}