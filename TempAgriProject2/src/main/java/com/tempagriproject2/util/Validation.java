package com.tempagriproject2.util;

import com.tempagriproject2.dto.AddressDTO;
import com.tempagriproject2.dto.CategoryDTO;
import com.tempagriproject2.dto.FarmerDTO;
import com.tempagriproject2.dto.UserDTO;
import com.tempagriproject2.enums.AddressType;
import com.tempagriproject2.enums.State;
import com.tempagriproject2.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Validation {

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

    public void farmerValidate(FarmerDTO farmerDTO) {
        // First, validate the user part (common fields like name, email, etc.)
        // FARM NAME
        if (farmerDTO == null) {
            throw new IllegalArgumentException(Constants.FARMER_DTO_NULL);
        }
        // Validate userId
        if (farmerDTO.getUserId() == null) {
            throw new IllegalArgumentException(Constants.USER_ID_NULL);
        }
        if (farmerDTO.getUserId() <= 0) {
            throw new IllegalArgumentException(Constants.USER_ID_INVALID);
        }
        if (isBlank(farmerDTO.getFarmName())) {
            throw new IllegalArgumentException(Constants.FARM_NAME_EMPTY);
        }
        if (farmerDTO.getFarmName().length() < 2 || farmerDTO.getFarmName().length() > 50) {
            throw new IllegalArgumentException(Constants.FARM_NAME_LENGTH);
        }

        // FARM TYPE
        if (isBlank(farmerDTO.getFarmType())) {
            throw new IllegalArgumentException(Constants.FARM_TYPE_EMPTY);
        }

        // LOCATION DESCRIPTION
        if (isBlank(farmerDTO.getLocationDiscription())) {
            throw new IllegalArgumentException(Constants.LOCATION_DESCRIPTION_EMPTY);
        }
        if (farmerDTO.getLocationDiscription().length() > 200) {
            throw new IllegalArgumentException(Constants.LOCATION_DESCRIPTION_TOO_LONG);
        }

        // GOVERNMENT ID
        if (isBlank(farmerDTO.getGovernmentId())) {
            throw new IllegalArgumentException(Constants.GOVERNMENT_ID_EMPTY);
        }
        if (!farmerDTO.getGovernmentId().matches("^[A-Z0-9]{6,20}$")) {
            throw new IllegalArgumentException(Constants.GOVERNMENT_ID_INVALID);
        }
    }

    // Utility
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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

        // Street
        if (isBlank(addressDTO.getStreet())) {
            errors.put("street",Constants.STREET_EMPTY);
        } else if (addressDTO.getStreet().length() > 100) {
            errors.put("street",Constants.STREET_TOO_LONG);
        }

        // District
        if (isBlank(addressDTO.getDistrict())) {
            errors.put("district", Constants.DISTRICT_EMPTY);
        } else if (addressDTO.getDistrict().length() > 50) {
            errors.put("district",Constants.DISTRICT_TOO_LONG);
        }

        // AddressType (enum ID check)
        try {
            AddressType.fromId(addressDTO.getAddressType());
        } catch (IllegalArgumentException e) {
            errors.put("addressType",Constants.ADDRESS_TYPE_INVALID);
        }

        // State (enum ID check)
        try {
            State.fromId(addressDTO.getState());
        } catch (IllegalArgumentException e) {
            errors.put("state",Constants.STATE_INVALID);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}
