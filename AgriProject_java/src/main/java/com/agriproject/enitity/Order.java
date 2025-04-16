package com.agriproject.enitity;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order extends BaseModel{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    private Date orderDate;
    private double totalAmount;
   @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
private List<OrderItem> orderItems;

}