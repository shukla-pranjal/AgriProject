package com.tempagriproject2.util;

public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // --------------------------------
    // ✅ Common Messages
    // --------------------------------
    private static final String EMPTY = " cannot be empty";
    private static final String NULL = " cannot be null";
    private static final String POSITIVE = " must be a positive number";
    private static final String INVALID = " is invalid";

    // --------------------------------
    // ✅ API Status
    // --------------------------------
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILED = "failed";
    public static final String MESSAGE_SUCCESS = "Operation Success";
    public static final String MESSAGE_FAILED = "Operation Failed";

    // --------------------------------
    // 👤 User Validation
    // --------------------------------
    public static final String USER_OBJECT_EMPTY = "User object/JSON" + EMPTY;
    public static final String NAME_EMPTY = "Name" + EMPTY;
    public static final String NAME_LENGTH = "Name must be between 3 and 50 characters";

    public static final String EMAIL_EMPTY = "Email" + EMPTY;
    public static final String EMAIL_INVALID = "Invalid email format";
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public static final String PASSWORD_EMPTY = "Password" + EMPTY;
    public static final String PASSWORD_LENGTH = "Password must be between 6 and 20 characters";

    public static final String PHONE_EMPTY = "Phone number" + EMPTY;
    public static final String PHONE_INVALID = "Phone number must be exactly 10 digits";
    public static final String PHONE_REGEX = "^[0-9]{10}$";

    // --------------------------------
    // 🌾 Farmer Validation
    // --------------------------------
    public static final String FARMER_DTO_NULL = "FarmerDTO" + NULL;
    public static final String USER_ID_NULL = "User ID" + NULL;
    public static final String USER_ID_INVALID = "User ID" + POSITIVE;

    public static final String FARM_NAME_EMPTY = "Farm name" + EMPTY;
    public static final String FARM_NAME_LENGTH = "Farm name must be between 2 and 50 characters";

    public static final String FARM_TYPE_EMPTY = "Farm type" + EMPTY;

    public static final String LOCATION_DESCRIPTION_EMPTY = "Farm location description" + EMPTY;
    public static final String LOCATION_DESCRIPTION_TOO_LONG = "Location description must not exceed 200 characters";

    public static final String GOVERNMENT_ID_EMPTY = "Government ID" + EMPTY;
    public static final String GOVERNMENT_ID_INVALID = "Government ID must be 6–20 characters long and contain only A–Z and 0–9";

    // --------------------------------
    // 🏡 Address Validation
    // --------------------------------
    public static final String ADDRESS_OBJECT_NULL = "Address object" + NULL;

    public static final String PIN_CODE_NULL = "Pin code" + NULL;
    public static final String PIN_CODE_LENGTH = "Pin code must be exactly 6 digits";

    public static final String STREET_EMPTY = "Street" + EMPTY;
    public static final String STREET_TOO_LONG = "Street must not exceed 100 characters";

    public static final String DISTRICT_EMPTY = "District" + EMPTY;
    public static final String DISTRICT_TOO_LONG = "District must not exceed 50 characters";

    public static final String ADDRESS_TYPE_INVALID = "Address Type" + INVALID;
    public static final String STATE_INVALID = "State" + INVALID;

    // --------------------------------
    // 📦 Product Validation
    // --------------------------------
    public static final String PRODUCT_DTO_NULL = "Product object" + NULL;

    public static final String PRODUCT_NAME_EMPTY = "Product name" + EMPTY;
    public static final String PRODUCT_NAME_LENGTH = "Product name must be between 2 and 100 characters";

    public static final String PRODUCT_DESCRIPTION_TOO_LONG = "Product description must not exceed 300 characters";

    public static final String CATEGORY_ID_NULL = "Category ID" + NULL;
    public static final String CATEGORY_ID_INVALID = "Category ID" + POSITIVE;

    public static final String PRODUCT_PRICE_NULL = "Product price" + NULL;
    public static final String PRODUCT_PRICE_INVALID = "Product price must be greater than 0";

    public static final String PRODUCT_QUANTITY_NULL = "Product quantity" + NULL;
    public static final String PRODUCT_QUANTITY_INVALID = "Product quantity must be greater than 0";

    public static final String UNIT_INVALID = "Unit" + INVALID;

    public static final String EXPIRY_DATE_INVALID = "Expiry date must be in the future";

    public static final String FARMER_ID_NULL = "Farmer ID" + NULL;
    public static final String FARMER_ID_INVALID = "Farmer ID" + POSITIVE;

    // --------------------------------
    // ⭐ Review Validation
    // --------------------------------
    public static final String REVIEW_OBJECT_NULL = "Review object" + NULL;

    public static final String RATING_NULL = "Rating" + NULL;
    public static final String RATING_INVALID = "Rating must be between 1 and 5";

    public static final String COMMENT_TOO_LONG = "Comment must not exceed 300 characters";

    public static final String PRODUCT_ID_NULL = "Product ID" + NULL;
    public static final String PRODUCT_ID_INVALID = "Product ID" + POSITIVE;

    public static final String REVIEW_DUPLICATE = "User has already reviewed this product";
}
