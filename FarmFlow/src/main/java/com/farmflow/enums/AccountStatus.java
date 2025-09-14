package com.farmflow.enums;

public enum AccountStatus {
    PENDING,    // Registered but not verified
    ACTIVE,     // Verified and can log in
    LOCKED,     // Temporarily blocked
    DISABLED    // User deactivated or admin banned
}
