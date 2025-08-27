package com.farmflow.entity;


import com.farmflow.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class AccountMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private AccountStatus status; // default

    private String verificationCode;
    private LocalDateTime verificationCodeCreatedAt;

    private String passwordResetToken;
    private LocalDateTime passwordResetTokenCreatedAt;

    private Integer failedLoginAttempts;
    private LocalDateTime lockedUntil;


    @OneToOne(mappedBy = "metadata", optional = false)
    private User user;
}