package com.farmflow.enums;

import lombok.Getter;

@Getter
public enum AddressType {
    HOME(1, "Home Address"),
    WORK(2, "Work Address"),
    SHIPPING(3, "Shipping Address"),
    BILLING(4, "Billing Address"),
    TEMPORARY(5, "Temporary Address"),
    FARM(6, "Farm Address");

    private final int id;
    private final String displayName;

    AddressType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static AddressType fromId(int id) {
        for (AddressType type : values()) {
            if (type.id == id) return type;
        }
        throw new IllegalArgumentException("Invalid Address Type ID: " + id);
    }

    public static AddressType fromDisplayName(String name) {
        for (AddressType type : values()) {
            if (type.displayName.equalsIgnoreCase(name)) return type;
        }
        throw new IllegalArgumentException("Invalid Address Type Name: " + name);
    }
}
