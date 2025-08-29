package com.farmflow.util;

import com.farmflow.dto.*;
import com.farmflow.dto.ml.ML5Dto;
import com.farmflow.dto.ml.ML6Dto;
import com.farmflow.dto.ml.ML7Dto;
import com.farmflow.enums.AddressType;
import com.farmflow.enums.EntityType;
import com.farmflow.enums.State;
import com.farmflow.enums.Unit;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.exception.ValidationException;
import com.farmflow.repository.FarmerRepository;
import com.farmflow.repository.ProductRepository;
import com.farmflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class Validation {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FarmerRepository farmerRepository;


    private void validateName(String name, Map<String, Object> errors) {
        if (ObjectUtils.isEmpty(name)) {
            errors.put("name", Constants.NAME_EMPTY);
        } else if (name.length() < 3 || name.length() > 50) {
            errors.put("name", Constants.NAME_LENGTH);
        }
    }

    private void validateEmail(String email, Map<String, Object> errors) {
        if (ObjectUtils.isEmpty(email)) {
            errors.put("email", Constants.EMAIL_EMPTY);
        } else if (!email.matches(Constants.EMAIL_REGEX)) {
            errors.put("email", Constants.EMAIL_INVALID);
        }
    }

    private void validatePassword(String password, Map<String, Object> errors) {
        if (ObjectUtils.isEmpty(password)) {
            errors.put("password", Constants.PASSWORD_EMPTY);
        } else if (password.length() < 6 || password.length() > 20) {
            errors.put("password", Constants.PASSWORD_LENGTH);
        }
    }

    private void validatePhone(String phone, Map<String, Object> errors) {
        if (ObjectUtils.isEmpty(phone)) {
            errors.put("phone", Constants.PHONE_EMPTY);
        } else if (!phone.matches(Constants.PHONE_REGEX)) {
            errors.put("phone", Constants.PHONE_INVALID);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public void userValidate(UserDTO userDTO) {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(userDTO)) {
            throw new IllegalArgumentException(Constants.USER_OBJECT_EMPTY);
        }

        validateName(userDTO.getName(), errors);
        validateEmail(userDTO.getEmail(), errors);
        validatePassword(userDTO.getPassword(), errors);
        validatePhone(userDTO.getPhone(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
    public void userValidateUpdate(UserDTO userDTO) {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(userDTO)) {
            throw new IllegalArgumentException(Constants.USER_OBJECT_EMPTY);
        }

        validateName(userDTO.getName(), errors);
        validatePhone(userDTO.getPhone(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void farmerValidate(FarmerDTO farmerDTO) {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (farmerDTO == null) {
            throw new IllegalArgumentException(Constants.FARMER_DTO_NULL);
        }

        if (farmerDTO.getUserId() == null || farmerDTO.getUserId() <= 0) {
            errors.put("userId", Constants.USER_ID_INVALID);
        }

        // Farm Name validation
        if (isBlank(farmerDTO.getFarmName())) {
            errors.put("farmName", Constants.FARM_NAME_EMPTY);
        } else if (farmerDTO.getFarmName().length() < 2 || farmerDTO.getFarmName().length() > 50) {
            errors.put("farmName", Constants.FARM_NAME_LENGTH);
        }

        // Farm Type validation
        if (isBlank(farmerDTO.getFarmType())) {
            errors.put("farmType", Constants.FARM_TYPE_EMPTY);
        }

        // Location Description validation
        if (isBlank(farmerDTO.getLocationDiscription())) {
            errors.put("locationDiscription", Constants.LOCATION_DESCRIPTION_EMPTY);
        } else if (farmerDTO.getLocationDiscription().length() > 200) {
            errors.put("locationDiscription", Constants.LOCATION_DESCRIPTION_TOO_LONG);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void categoryValidate(CategoryDTO categoryDTO) {
        Map<String, Object> errors = new LinkedHashMap<>();
        validateName(categoryDTO.getName(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void addressValidate(AddressDTO addressDTO) {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (addressDTO == null) {
            throw new IllegalArgumentException(Constants.ADDRESS_OBJECT_NULL);
        }

        if (addressDTO.getPinCode() == null) {
            errors.put("pinCode",Constants.PIN_CODE_NULL);
        } else if (String.valueOf(addressDTO.getPinCode()).length() != 6) {
            errors.put("pinCode", Constants.PIN_CODE_LENGTH);
        }

        if (isBlank(addressDTO.getStreet())) {
            errors.put("street",Constants.STREET_EMPTY);
        } else if (addressDTO.getStreet().length() > 100) {
            errors.put("street",Constants.STREET_TOO_LONG);
        }

        if (isBlank(addressDTO.getDistrict())) {
            errors.put("district", Constants.DISTRICT_EMPTY);
        } else if (addressDTO.getDistrict().length() > 50) {
            errors.put("district",Constants.DISTRICT_TOO_LONG);
        }

        try {
            AddressType.fromId(addressDTO.getAddressType());
        } catch (IllegalArgumentException e) {
            errors.put("addressType",Constants.ADDRESS_TYPE_INVALID);
        }

        try {
            State.fromId(addressDTO.getState());
        } catch (IllegalArgumentException e) {
            errors.put("state",Constants.STATE_INVALID);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void productValidate(ProductDTO productDTO) {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (productDTO == null) {
            throw new IllegalArgumentException(Constants.PRODUCT_DTO_NULL);
        }

        if (isBlank(productDTO.getName())) {
            errors.put("name", Constants.PRODUCT_NAME_EMPTY);
        } else if (productDTO.getName().length() < 2 || productDTO.getName().length() > 100) {
            errors.put("name", Constants.PRODUCT_NAME_LENGTH);
        }

        if (!isBlank(productDTO.getDescription()) && productDTO.getDescription().length() > 300) {
            errors.put("description", Constants.PRODUCT_DESCRIPTION_TOO_LONG);
        }

        if (productDTO.getCategoryId() == null) {
            errors.put("categoryId", Constants.CATEGORY_ID_NULL);
        } else if (productDTO.getCategoryId() <= 0) {
            errors.put("categoryId", Constants.CATEGORY_ID_INVALID);
        }

        if (productDTO.getPrice() == null) {
            errors.put("price", Constants.PRODUCT_PRICE_NULL);
        } else if (productDTO.getPrice() <= 0) {
            errors.put("price", Constants.PRODUCT_PRICE_INVALID);
        }

        if (productDTO.getQuantity() == null) {
            errors.put("quantity", Constants.PRODUCT_QUANTITY_NULL);
        } else if (productDTO.getQuantity() <= 0) {
            errors.put("quantity", Constants.PRODUCT_QUANTITY_INVALID);
        }

        try {
            Unit.fromId(productDTO.getUnit());
        } catch (IllegalArgumentException e) {
            errors.put("unit", Constants.UNIT_INVALID);
        }

        if (productDTO.getExpiryDate() != null && productDTO.getExpiryDate().isBefore(LocalDateTime.now())) {
            errors.put("expiryDate", Constants.EXPIRY_DATE_INVALID);
        }

        if (productDTO.getFarmerId() == null) {
            errors.put("farmerId", Constants.FARMER_ID_NULL);
        } else if (productDTO.getFarmerId() <= 0) {
            errors.put("farmerId", Constants.FARMER_ID_INVALID);
        }

        if (productDTO.getAvailable() == null)
            errors.put("available", Constants.AVAILABLE_NULL);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void reviewValidate(ReviewDTO reviewDTO) {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (reviewDTO == null) {
            throw new IllegalArgumentException(Constants.REVIEW_OBJECT_NULL);
        }

        if (reviewDTO.getRating() == null) {
            errors.put("rating", Constants.RATING_NULL);
        } else if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            errors.put("rating", Constants.RATING_INVALID);
        }
        if (reviewDTO.getComment() != null && reviewDTO.getComment().length() > 300) {
            errors.put("comment", Constants.COMMENT_TOO_LONG);
        }

        if (reviewDTO.getProductId() == null) {
            errors.put("productId", Constants.PRODUCT_ID_NULL);
        } else if (reviewDTO.getProductId() <= 0) {
            errors.put("productId", Constants.PRODUCT_ID_INVALID);
        }

        if (reviewDTO.getUserId() == null) {
            errors.put("userId", Constants.USER_ID_NULL);
        } else if (reviewDTO.getUserId() <= 0) {
            errors.put("userId", Constants.USER_ID_INVALID);
        }


        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void cartItemValidate(CartItemDTO itemDTO) {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (itemDTO == null) {
            throw new IllegalArgumentException("CartItemDTO cannot be null.");
        }

        if (itemDTO.getProductId() == null) {
            errors.put("productId", Constants.PRODUCT_ID_NULL);
        } else if (itemDTO.getProductId() <= 0) {
            errors.put("productId", Constants.PRODUCT_ID_INVALID);
        }

        if (itemDTO.getQuantity() == null) {
            errors.put("quantity", Constants.CART_ITEM_QUANTITY_NULL);
        } else if (itemDTO.getQuantity() <= 0) {
            errors.put("quantity", Constants.CART_ITEM_QUANTITY_INVALID);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateOrder(OrdersDTO ordersDTO) {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (ordersDTO == null) {
            throw new IllegalArgumentException(Constants.ORDER_OBJECT_NULL);
        }

        if (ordersDTO.getUserId() == null || ordersDTO.getUserId() <= 0) {
            errors.put("userId", Constants.USER_ID_INVALID);
        }

        if (ordersDTO.getItems() == null || ordersDTO.getItems().isEmpty()) {
            errors.put("items", Constants.ORDER_ITEMS_EMPTY);
        } else {
            for (int i = 0; i < ordersDTO.getItems().size(); i++) {
                OrderItemDTO item = ordersDTO.getItems().get(i);
                String itemPrefix = "items[" + i + "]";
                if (item.getProductId() == null || item.getProductId() <= 0) {
                    errors.put(itemPrefix + ".productId", Constants.PRODUCT_ID_INVALID);
                }
                if (item.getQuantity() == null || item.getQuantity() <= 0) {
                    errors.put(itemPrefix + ".quantity", Constants.ORDER_ITEM_QUANTITY_INVALID);
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void paymentValidate(PaymentDTO paymentDTO) {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (paymentDTO == null) {
            throw new IllegalArgumentException(Constants.PAYMENT_OBJECT_NULL);
        }

        if (isBlank(paymentDTO.getTransactionId())) {
            errors.put("transactionId", Constants.TRANSACTION_ID_EMPTY);
        }

        if (paymentDTO.getAmount() == null) {
            errors.put("amount", Constants.AMOUNT_NULL);
        } else if (paymentDTO.getAmount() <= 0) {
            errors.put("amount", Constants.AMOUNT_INVALID);
        }

        if (paymentDTO.getOrdersId() == null) {
            errors.put("ordersId", Constants.ORDER_ID_NULL);
        } else if (paymentDTO.getOrdersId() <= 0) {
            errors.put("ordersId", Constants.ORDER_ID_INVALID);
        }

        if (paymentDTO.getPaymentMethod() == null) {
            errors.put("paymentMethod", Constants.PAYMENT_METHOD_NULL);
        }

        if (paymentDTO.getPaymentStatus() == null) {
            errors.put("paymentStatus", Constants.PAYMENT_STATUS_NULL);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }


    public void imageValidate(MultipartFile file, EntityType entityType, Integer entityId) throws Exception {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (file == null || file.isEmpty()) {
            errors.put("file", Constants.IMAGE_FILE_EMPTY);
        } else if (!isSupportedImageType(file.getContentType())) {
            errors.put("file", Constants.IMAGE_FILE_UNSUPPORTED_TYPE);
        }

        if (entityType == null) {
            errors.put("entityType", Constants.ENTITY_TYPE_NULL);
        }

        if (entityId == null || entityId <= 0) {
            errors.put("entityId", Constants.ENTITY_ID_INVALID);
        } else {
            try {
                entityExists(entityType, entityId);
            } catch (ResourceNotFoundException e) {
                errors.put("entity", Constants.ENTITY_NOT_FOUND);
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private boolean isSupportedImageType(String contentType) {
        return contentType != null &&
                (contentType.equalsIgnoreCase("image/jpeg") ||
                        contentType.equalsIgnoreCase("image/jpg") ||
                        contentType.equalsIgnoreCase("image/png") ||
                        contentType.equalsIgnoreCase("image/gif"));
    }
    public void entityExists(EntityType entityType, Integer entityId) throws ResourceNotFoundException {
        boolean exists;

        if (entityType == null || entityId == null || entityId <= 0) {
            throw new IllegalArgumentException(Constants.ENTITY_TYPE_NULL + " or " + Constants.ENTITY_ID_INVALID);
        }

        exists = switch (entityType) {
            case USER -> userRepository.existsById(entityId);
            case PRODUCT -> productRepository.existsById(entityId);
            case FARMER -> farmerRepository.existsById(entityId);
            default -> throw new IllegalArgumentException("Unsupported EntityType: " + entityType);
        };

        if (!exists) {
            throw new ResourceNotFoundException(Constants.ENTITY_NOT_FOUND);
        }
    }


    public void model5Validation(ML5Dto ml5Dto) {
        Map<String, Object> error = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(ml5Dto)) {
            throw new IllegalArgumentException("Category Object/JSON should not be empty");
        }
        if (ml5Dto.getN() == null || ml5Dto.getN() < 0 || ml5Dto.getN() > 145) {
            error.put("n", "Nitrogen (N) must be between 0 and 145");
        }

        // Phosphorus (P) validation
        if (ml5Dto.getP() == null || ml5Dto.getP() < 0 || ml5Dto.getP() > 150) {
            error.put("p", "Phosphorus (P) must be between 0 and 150");
        }

        // Potassium (K) validation
        if (ml5Dto.getK() == null || ml5Dto.getK() < 0 || ml5Dto.getK() > 210) {
            error.put("k", "Potassium (K) must be between 0 and 210");
        }

        // Temperature validation
        if (ml5Dto.getTemperature() == null || ml5Dto.getTemperature() < 8 || ml5Dto.getTemperature() > 44) {
            error.put("temperature", "Temperature must be between 8°C and 44°C");
        }

        // Humidity validation
        if (ml5Dto.getHumidity() == null || ml5Dto.getHumidity() < 14 || ml5Dto.getHumidity() > 99.99) {
            error.put("humidity", "Humidity must be between 14% and 99.99%");
        }

        // pH validation
        if (ml5Dto.getPh() == null || ml5Dto.getPh() < 3 || ml5Dto.getPh() > 10) {
            error.put("ph", "pH must be between 3 and 10");
        }

        // Rainfall validation
        if (ml5Dto.getRainfall() == null || ml5Dto.getRainfall() < 10 || ml5Dto.getRainfall() > 300) {
            error.put("rainfall", "Rainfall must be between 10mm and 300mm");
        }

        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }

    }


    public void model6Validation(ML6Dto ml6Dto) {
        Map<String, Object> error = new LinkedHashMap<>();

        // Check if the object is null
        if (ObjectUtils.isEmpty(ml6Dto)) {
            throw new IllegalArgumentException("ML6Dto JSON object should not be empty");
        }

        // Moisture validation
        if (ml6Dto.getMoisture() == null || ml6Dto.getMoisture() < 4 || ml6Dto.getMoisture() > 1023) {
            error.put("moisture", "Moisture must be between 4 and 1023");
        }

        // Temperature validation
        if (ml6Dto.getTemp() == null || ml6Dto.getTemp() < 9 || ml6Dto.getTemp() > 46) {
            error.put("temp", "Temperature must be between 9°C and 46°C");
        }

        // If any validation errors exist, throw a ValidationException
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }


    public void model7Validation(ML7Dto ml7Dto) {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(ml7Dto)) {
            throw new IllegalArgumentException("ML7Dto JSON object should not be empty");
        }

        validateRange(ml7Dto.getTemparature(), 24, 39, "temparature", errors);
        validateRange(ml7Dto.getHumidity(), 49, 73, "humidity", errors);
        validateRange(ml7Dto.getMoisture(), 24, 66, "moisture", errors);
        validateRange(ml7Dto.getNitrogen(), 3, 43, "nitrogen", errors);
        validateRange(ml7Dto.getPotassium(), 0, 20, "potassium", errors);
        validateRange(ml7Dto.getPhosphorous(), 0, 43, "phosphorous", errors);

        validateCategory(ml7Dto.getSoilType(), Constants.ALLOWED_SOIL_TYPES, "soilType", errors);
        validateCategory(ml7Dto.getCropType(), Constants.ALLOWED_CROP_TYPES, "cropType", errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateRange(Integer value, int min, int max, String fieldName, Map<String, Object> errors) {
        if (value == null) {
            errors.put(fieldName, fieldName + " should not be null");
        } else if (value < min || value > max) {
            errors.put(fieldName, fieldName + " must be between " + min + " and " + max);
        }
    }

    private void validateCategory(String value, List<String> validValues, String fieldName, Map<String, Object> errors) {
        if (ObjectUtils.isEmpty(value)) {
            errors.put(fieldName, fieldName + " should not be empty");
        } else if (!validValues.contains(value)) {
            errors.put(fieldName, fieldName + " must be one of " + validValues);
        }
    }


}
