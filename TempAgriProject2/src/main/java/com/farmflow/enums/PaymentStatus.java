package com.farmflow.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING(1, "Pending"),
    COMPLETED(2, "Completed"),
    FAILED(3, "Failed"),
    CANCELLED(4, "Cancelled");

    private final int id;
    private final String displayName;

    PaymentStatus(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static PaymentStatus fromId(int id) {
        for (PaymentStatus status : values()) {
            if (status.id == id) return status;
        }
        throw new IllegalArgumentException("Invalid Payment Status ID: " + id);
    }

    public static PaymentStatus fromDisplayName(String name) {
        for (PaymentStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(name)) return status;
        }
        throw new IllegalArgumentException("Invalid Payment Status name: " + name);
    }
}

