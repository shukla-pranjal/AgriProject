package com.farmflow.util;

import java.util.List;
/**
 * Centralized constants for the application
 * Organized by functional domains with clear grouping
 */
public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // --------------------------------
    // 🏷️ Common Formatting & Patterns
    // --------------------------------
    private static final String EMPTY = " cannot be empty";
    private static final String NULL = " cannot be null";
    private static final String POSITIVE = " must be a positive number";
    private static final String INVALID = " is invalid";

    // --------------------------------
    // 🔒 Security & Authentication
    // --------------------------------
    public static final String ACCESS_DENIED = "Access Denied";
        public static final String ADMIN = "hasRole('ADMIN')";
    public static final String ADMIN_OR_USER = "hasAnyRole('ADMIN','USER')";
    public static final String ADMIN_OR_FARMER = "hasAnyRole('ADMIN','FARMER')";
    public static final String USER_OR_FARMER = "hasAnyRole('USER','FARMER')";
    public static final String ADMIN_USER_FARMER = "hasAnyRole('ADMIN','USER','FARMER')";

    public static final long TOKEN_EXPIRES_IN_MILLISECOND = 24 * 60 * 60 * 1000; // 24 hours

    public static final String[] PUBLIC_PATHS = {
            "/api/v1/auth/**",
            "/actuator/**",
            "/farmer_app_shopping-docs",
            "/farmer_app_shopping-docs/**",
            "/farmer_app_shopping-doc/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    // --------------------------------
    // 📦 Domain Entities
    // --------------------------------
    public static final String USER = "User";
    public static final String FARMER = "Farmer";
    public static final String PRODUCT = "Product";
    public static final String CATEGORY = "Category";
    public static final String ORDER = "Order";
    public static final String PAYMENT = "Payment";
    public static final String REVIEW = "Review";
    public static final String CART = "Cart";
    public static final String ADDRESS = "Address";
    public static final String IMAGE = "Image";

    // --------------------------------
    // 🔄 API Response Statuses
    // --------------------------------
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILED = "failed";
    public static final String MESSAGE_SUCCESS = "Operation Success";
    public static final String MESSAGE_FAILED = "Operation Failed";

    // --------------------------------
    // 🔍 Resource Not Found Messages
    // --------------------------------
    public static final String RESOURCE_NOT_FOUND = "%s not found with ID: %d";
    public static final String ENTITY_NOT_FOUND = "Associated entity not found";

    // --------------------------------
    // ⚠️ Validation Messages
    // --------------------------------

    // 🔹 Common Validation
    public static final String ID_POSITIVE = "ID" + POSITIVE;
    public static final String ENTITY_TYPE_NULL = "Entity type" + NULL;
    public static final String ENTITY_ID_INVALID = "Entity ID" + POSITIVE;

    // 👤 User Validation
    public static final String USER_OBJECT_EMPTY = "User object" + EMPTY;
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
    public static final String USER_EMAIL_NOT_EXISTS = "User with email not exists";
    public static final String ALREADY_ADMIN = "User is already an admin";
    public static final String NOT_ADMIN = "User is not an admin";

    // 🌾 Farmer Validation
    public static final String FARMER_DTO_NULL = "FarmerDTO" + NULL;
    public static final String USER_ID_NULL = "User ID" + NULL;
    public static final String USER_ID_INVALID = "User ID" + POSITIVE;
    public static final String FARM_NAME_EMPTY = "Farm name" + EMPTY;
    public static final String FARM_NAME_LENGTH = "Farm name must be between 2 and 50 characters";
    public static final String FARM_TYPE_EMPTY = "Farm type" + EMPTY;
    public static final String LOCATION_DESCRIPTION_EMPTY = "Location description" + EMPTY;
    public static final String LOCATION_DESCRIPTION_TOO_LONG = "Location description must not exceed 200 characters";
    public static final String USER_ALREADY_FARMER = "User with ID %d is already registered as a Farmer";
    public static final String INVALID_FARMER_DATA = "Invalid farmer data: %s";

    // 🏡 Address Validation
    public static final String ADDRESS_OBJECT_NULL = "Address object" + NULL;
    public static final String PIN_CODE_NULL = "Pin code" + NULL;
    public static final String PIN_CODE_LENGTH = "Pin code must be exactly 6 digits";
    public static final String STREET_EMPTY = "Street" + EMPTY;
    public static final String STREET_TOO_LONG = "Street must not exceed 100 characters";
    public static final String DISTRICT_EMPTY = "District" + EMPTY;
    public static final String DISTRICT_TOO_LONG = "District must not exceed 50 characters";
    public static final String ADDRESS_TYPE_INVALID = "Address Type" + INVALID;
    public static final String STATE_INVALID = "State" + INVALID;

    // 📦 Product Validation
    public static final String PRODUCT_DTO_NULL = "Product object" + NULL;
    public static final String PRODUCT_NAME_EMPTY = "Product name" + EMPTY;
    public static final String PRODUCT_NAME_LENGTH = "Product name must be between 2 and 100 characters";
    public static final String PRODUCT_DESCRIPTION_TOO_LONG = "Product description must not exceed 300 characters";
    public static final String CATEGORY_ID_NULL = "Category ID" + NULL;
    public static final String CATEGORY_ID_INVALID = "Category ID" + POSITIVE;
    public static final String FARMER_ID_NULL = "Farmer ID" + NULL;
    public static final String FARMER_ID_INVALID = "Farmer ID" + POSITIVE;
    public static final String PRODUCT_PRICE_NULL = "Product price" + NULL;
    public static final String PRODUCT_PRICE_INVALID = "Product price must be greater than 0";
    public static final String PRODUCT_QUANTITY_NULL = "Product quantity" + NULL;
    public static final String PRODUCT_QUANTITY_INVALID = "Product quantity must be greater than 0";
    public static final String UNIT_INVALID = "Unit" + INVALID;
    public static final String EXPIRY_DATE_INVALID = "Expiry date must be in the future";
    public static final String AVAILABLE_NULL = "Available" + NULL;
    public static final String PRODUCT_NOT_AVAILABLE = "Product %s is not available";
    public static final String INSUFFICIENT_STOCK = "Insufficient stock for product %s";
    public static final String CATEGORY_ID_REQUIRED = "Category ID is required";
    public static final String FARMER_ID_REQUIRED = "Farmer ID is required";
    public static final String CATEGORY_AND_FARMER_REQUIRED = "Category ID and Farmer ID are required";

    // ⭐ Review Validation
    public static final String REVIEW_OBJECT_NULL = "Review object" + NULL;
    public static final String RATING_NULL = "Rating" + NULL;
    public static final String RATING_INVALID = "Rating must be between 1 and 5";
    public static final String COMMENT_TOO_LONG = "Comment must not exceed 300 characters";
    public static final String PRODUCT_ID_NULL = "Product ID" + NULL;
    public static final String PRODUCT_ID_INVALID = "Product ID" + POSITIVE;
    public static final String REVIEW_DUPLICATE = "User has already reviewed this product";

    // 🛒 Cart Validation
    public static final String CART_ITEM_QUANTITY_NULL = "Cart item quantity" + NULL;
    public static final String CART_ITEM_QUANTITY_INVALID = "Cart item quantity" + POSITIVE;
    public static final String CART_EMPTY = "Cart is empty. Cannot place orders";
    public static final String USER_HAS_CART = "User with ID: %d already has a cart";
    public static final String PRODUCT_NOT_IN_CART = "Product not found in cart with ID: %d";
    public static final String QUANTITY_MUST_BE_POSITIVE = "Quantity must be positive";
    public static final String AMOUNT_MUST_BE_POSITIVE = "Amount must be positive";

    // 📦 Order Validation
    public static final String ORDER_OBJECT_NULL = "Order object" + NULL;
    public static final String ORDER_ITEMS_EMPTY = "Order items" + EMPTY;
    public static final String ORDER_ITEM_QUANTITY_INVALID = "Order item quantity" + POSITIVE;
    public static final String ORDER_NOT_FOUND = "Original order not found with ID: %d";
    public static final String CANNOT_CANCEL_DELIVERED = "Delivered orders cannot be cancelled";
    public static final String USER_ID_REQUIRED = "User ID is required to create an order";
    public static final String PRODUCT_ID_REQUIRED = "Product ID is required in order items";

    // 💳 Payment Validation
    public static final String PAYMENT_OBJECT_NULL = "Payment object" + NULL;
    public static final String TRANSACTION_ID_EMPTY = "Transaction ID" + EMPTY;
    public static final String AMOUNT_NULL = "Amount" + NULL;
    public static final String AMOUNT_INVALID = "Amount" + POSITIVE;
    public static final String ORDER_ID_NULL = "Order ID" + NULL;
    public static final String ORDER_ID_INVALID = "Order ID" + POSITIVE;
    public static final String PAYMENT_METHOD_NULL = "Payment method" + NULL;
    public static final String PAYMENT_STATUS_NULL = "Payment status" + NULL;
    public static final String PAYMENT_EXISTS_FOR_ORDER = "Payment already exists for order ID: %d";
    public static final String PAYMENT_CANCELLED_ORDER = "Cannot make payment for cancelled order with ID: %d";
    public static final String PAYMENT_NOT_FOUND_FOR_ORDER = "Payment not found for Order ID: %d";
    public static final String CANNOT_DOWNGRADE_COMPLETED = "Cannot downgrade a completed payment";
    public static final String ORDER_ID_REQUIRED = "Order ID is required for payment";
    public static final String EMAIL_IN_USE = "Email %s is already in use";

    // 🖼️ Image Validation
    public static final String IMAGE_FILE_EMPTY = "Image file" + EMPTY;
    public static final String IMAGE_FILE_UNSUPPORTED_TYPE = "Image file type" + INVALID;
    public static final int MAX_IMAGES_FARMER = 5;
    public static final int MAX_IMAGES_PRODUCT = 10;
    public static final String MAX_IMAGES_REACHED = "%s with ID %d already has the maximum number of images (%d)";
    public static final String UNSUPPORTED_ENTITY_TYPE = "Unsupported entity type: %s";
    public static final String IMAGE_NOT_BELONG_TO_ENTITY = "Image with ID %d does not belong to %s with ID %d";

    // 🌱 Agricultural Constants
    public static final List<String> ALLOWED_SOIL_TYPES = List.of(
            "Sandy", "Loamy", "Black", "Red", "Clayey"
    );

    public static final List<String> ALLOWED_CROP_TYPES = List.of(
            "Maize", "Sugarcane", "Cotton", "Tobacco", "Paddy",
            "Barley", "Wheat", "Millets", "Pulses", "Groundnuts", "Oilseeds"
    );



    // Email constants
    public static final String VERIFICATION_SUBJECT = "Verify Your Email Address";
    public static final String TEMPLATE_NAME = "verification-email";
}