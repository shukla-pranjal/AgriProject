package com.tempagriproject2.enums;

import lombok.Getter;

@Getter
public enum State {
    ANDHRA_PRADESH(1, "AP", "Andhra Pradesh"),
    ARUNACHAL_PRADESH(2, "AR", "Arunachal Pradesh"),
    ASSAM(3, "AS", "Assam"),
    BIHAR(4, "BR", "Bihar"),
    CHHATTISGARH(5, "CG", "Chhattisgarh"),
    GOA(6, "GA", "Goa"),
    GUJARAT(7, "GJ", "Gujarat"),
    HARYANA(8, "HR", "Haryana"),
    HIMACHAL_PRADESH(9, "HP", "Himachal Pradesh"),
    JHARKHAND(10, "JH", "Jharkhand"),
    KARNATAKA(11, "KA", "Karnataka"),
    KERALA(12, "KL", "Kerala"),
    MADHYA_PRADESH(13, "MP", "Madhya Pradesh"),
    MAHARASHTRA(14, "MH", "Maharashtra"),
    MANIPUR(15, "MN", "Manipur"),
    MEGHALAYA(16, "ML", "Meghalaya"),
    MIZORAM(17, "MZ", "Mizoram"),
    NAGALAND(18, "NL", "Nagaland"),
    ODISHA(19, "OR", "Odisha"),
    PUNJAB(20, "PB", "Punjab"),
    RAJASTHAN(21, "RJ", "Rajasthan"),
    SIKKIM(22, "SK", "Sikkim"),
    TAMIL_NADU(23, "TN", "Tamil Nadu"),
    TELANGANA(24, "TS", "Telangana"),
    TRIPURA(25, "TR", "Tripura"),
    UTTAR_PRADESH(26, "UP", "Uttar Pradesh"),
    UTTARAKHAND(27, "UK", "Uttarakhand"),
    WEST_BENGAL(28, "WB", "West Bengal"),
    DELHI(29, "DL", "Delhi"),
    JAMMU_AND_KASHMIR(30, "JK", "Jammu and Kashmir"),
    LADAKH(31, "LA", "Ladakh"),
    PUDUCHERRY(32, "PY", "Puducherry"),
    CHANDIGARH(33, "CH", "Chandigarh"),
        ANDAMAN_AND_NICOBAR_ISLANDS(34, "AN", "Andaman and Nicobar Islands"),
    DADRA_AND_NAGAR_HAVELI_AND_DAMAN_AND_DIU(35, "DNDD", "Dadra and Nagar Haveli and Daman and Diu"),
    LAKSHADWEEP(36, "LD", "Lakshadweep");

    private final int id;
    private final String code;
    private final String fullName;

    State(int id, String code, String fullName) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
    }

    public static State fromId(int id) {
        for (State state : values()) {
            if (state.id == id) return state;
        }
        throw new IllegalArgumentException("Invalid State ID: " + id);
    }

    public static State fromCode(String code) {
        for (State state : values()) {
            if (state.code.equalsIgnoreCase(code)) return state;
        }
        throw new IllegalArgumentException("Invalid State Code: " + code);
    }
}
