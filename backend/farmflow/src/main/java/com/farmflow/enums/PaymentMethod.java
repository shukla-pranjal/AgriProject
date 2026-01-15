package com.farmflow.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CARD(1, "Card"),
    UPI(2, "UPI"),
    COD(3, "Cash on Delivery"),
    WALLET(4, "Wallet");

    private final int id;
    private final String displayName;

    PaymentMethod(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static PaymentMethod fromId(int id) {
        for (PaymentMethod method : values()) {
            if (method.id == id) return method;
        }
        throw new IllegalArgumentException("Invalid Payment Method ID: " + id);
    }

    public static PaymentMethod fromDisplayName(String name) {
        for (PaymentMethod method : values()) {
            if (method.displayName.equalsIgnoreCase(name)) return method;
        }
        throw new IllegalArgumentException("Invalid Payment Method name: " + name);
    }
}

