package com.farmflow.enums;

import lombok.Getter;

@Getter
public enum OrdersStatus {
    PENDING(1, "Pending - Order created, waiting for payment"),
    PAID(2, "Paid - Payment completed"),
    CONFIRMED(3, "Confirmed - Ready for shipping"),
    SHIPPED(4, "Shipped - In transit"),
    DELIVERED(5, "Delivered - Order completed"),
    CANCELLED(6, "Cancelled - Order voided");

    private final int id;
    private final String displayName;

    OrdersStatus(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static OrdersStatus fromId(int id) {
        for (OrdersStatus status : values()) {
            if (status.id == id) return status;
        }
        throw new IllegalArgumentException("Invalid Orders Status ID: " + id);
    }

    public static OrdersStatus fromDisplayName(String name) {
        for (OrdersStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(name)) return status;
        }
        throw new IllegalArgumentException("Invalid Orders Status name: " + name);
    }
}
