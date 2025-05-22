package com.farmflow.dto;

import com.farmflow.enums.OrdersStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDTO {
    private Integer id;
    private Integer userId;
    private LocalDateTime orderDate;
    private OrdersStatus status;
    private Double totalPrice;
    private List<OrderItemDTO> items;
}
