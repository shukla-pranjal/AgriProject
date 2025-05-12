package com.tempagriproject2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum Unit {
    KG(1), TON(2), LITRE(3), GRAM(4), QUINTAL(5), BAG(6);

    private final int id;

    Unit(int id) {
        this.id = id;
    }

    public static Unit fromId(int id) {
        for (Unit unit : values()) {
            if (unit.getId() == id) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid unit id: " + id);
    }
}
