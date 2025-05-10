package com.tempagriproject2.util;

public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // These constants are used in Common Utils
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILED = "failed";
    public static final String MESSAGE_FAILED = "Operation Failed";
    public static final String MESSAGE_SUCCESS = "Operation Success";

    // ----------------------------
    // 🔁 Shared / Base Constants
    // ----------------------------
    private static final String EMPTY_MESSAGE = " should not be empty";
    private static final String NULL_MESSAGE = " cannot be null";

    // ----------------------------
    // 👤 User Validation
    // ----------------------------
    public static final String USER_OBJECT_EMPTY = "User object/JSON" + EMPTY_MESSAGE;

    public static final String NAME_EMPTY = "Name" + EMPTY_MESSAGE;
    public static final String NAME_LENGTH = "Name should be between 3 and 50 characters.";

    public static final String EMAIL_EMPTY = "Email" + EMPTY_MESSAGE;
    public static final String EMAIL_INVALID = "Invalid email format.";
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public static final String PASSWORD_EMPTY = "Password" + EMPTY_MESSAGE;
    public static final String PASSWORD_LENGTH = "Password should be between 6 and 20 characters.";

    public static final String PHONE_EMPTY = "Phone number" + EMPTY_MESSAGE;
    public static final String PHONE_INVALID = "Phone number should be exactly 10 digits.";
    public static final String PHONE_REGEX = "^[0-9]{10}$";

    // ----------------------------
    // 🌾 Farmer Validation
    // ----------------------------
    public static final String FARMER_DTO_NULL = "FarmerDTO"+NULL_MESSAGE;

    public static final String USER_ID_NULL = "User ID"+NULL_MESSAGE;
    public static final String USER_ID_INVALID = "User ID must be a positive integer";


    public static final String FARM_NAME_EMPTY = "Farm name" + EMPTY_MESSAGE;
    public static final String FARM_NAME_LENGTH = "Farm name must be between 2 and 50 characters.";

    public static final String FARM_TYPE_EMPTY = "Farm type" + EMPTY_MESSAGE;

    public static final String LOCATION_DESCRIPTION_EMPTY = "Farm location description" + EMPTY_MESSAGE;
    public static final String LOCATION_DESCRIPTION_TOO_LONG = "Location description can't exceed 200 characters.";

    public static final String GOVERNMENT_ID_EMPTY = "Government ID" + EMPTY_MESSAGE;
    public static final String GOVERNMENT_ID_INVALID = "Government ID must be 6–20 characters long and contain only A–Z and 0–9.";

    // ----------------------------
    // Address Validation
    // ----------------------------
    public static final String ADDRESS_OBJECT_NULL = "Address object cannot be null";
    public static final String PIN_CODE_NULL = "Pin code is required";
    public static final String PIN_CODE_LENGTH = "Pin code must be exactly 6 digits";

    public static final String STREET_EMPTY = "Street cannot be empty";
    public static final String STREET_TOO_LONG = "Street name is too long (max 100 characters)";

    public static final String DISTRICT_EMPTY = "District cannot be empty";
    public static final String DISTRICT_TOO_LONG = "District name is too long (max 50 characters)";

    public static final String ADDRESS_TYPE_INVALID = "Invalid Address Type ID";
    public static final String STATE_INVALID = "Invalid State ID";

}