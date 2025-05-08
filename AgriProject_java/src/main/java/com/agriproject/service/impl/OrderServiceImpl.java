package com.agriproject.service.impl;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agriproject.dto.OrderDTO;
import com.agriproject.enitity.Order;
import com.agriproject.enitity.User;
import com.agriproject.exception.ResourceNotFoundException;
import com.agriproject.repository.OrderRepository;
import com.agriproject.repository.UserRepository;
import com.agriproject.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long id)throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderDTO.class);
    }

    @Override
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) throws Exception {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        modelMapper.map(orderDTO, existing);
        Order updated = orderRepository.save(existing);
        return modelMapper.map(updated, OrderDTO.class);
    }

    @Override
    public void deleteOrder(Long id) throws Exception{
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        orderRepository.delete(existing);
    }

  
    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with User ID: " + userId));
        return orderRepository.findByUser(user).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }
   
}