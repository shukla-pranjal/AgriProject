package com.farmflow.entity;

import com.farmflow.enums.AddressType;
import com.farmflow.enums.State;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Address extends BaseModel{
    private Integer pinCode;
    private String street;
    private String district;
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    @Enumerated(EnumType.STRING)
    private State state;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
}
