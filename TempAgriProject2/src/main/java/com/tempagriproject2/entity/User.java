package com.tempagriproject2.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseModel {
    private String name;
    private String email;
    private String password;
    private String phone;
    // private String role;
}