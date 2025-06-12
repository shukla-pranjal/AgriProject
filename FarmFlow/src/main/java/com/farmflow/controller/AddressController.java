package com.farmflow.controller;

import com.farmflow.config.AuditAwareConfig;
import com.farmflow.dto.AddressDTO;
import com.farmflow.dto.PaginatedResponse;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.endpoint.AddressEndpoint;
import com.farmflow.service.AddressService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController implements AddressEndpoint {

    private final AddressService addressService;
    private final AuditAwareConfig auditAwareConfig;

    @Override
    public ResponseEntity<?> getAll() {
        List<AddressDTO> addressList = addressService.getAllAddresses();
        return CommonUtil.createBuildResponse(addressList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        AddressDTO address = addressService.getAddressById(id);
        return CommonUtil.createBuildResponse(address, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> create(AddressDTO addressDTO) throws Exception {
        AddressDTO created = addressService.createAddress(addressDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> update(Integer id, AddressDTO addressDTO) throws Exception {
        AddressDTO updated = addressService.updateAddress(id, addressDTO);
        return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        addressService.deleteAddress(id);
        return CommonUtil.createBuildResponseMessage("Address deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByUserId(Integer userId) throws Exception {
        List<AddressDTO> addresses = addressService.getAddressesByUserId(userId);
        return CommonUtil.createBuildResponse(addresses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchAddresses(Integer pinCode, String district, String street, Integer state, Integer addressType, Integer userId) {
        List<AddressDTO> results = addressService.searchAddresses(pinCode, district, street, state, addressType, userId);
        return CommonUtil.createBuildResponse(results, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPaged(PaginationRequest paginationRequest) {
        Page<AddressDTO> page = addressService.getAllPaged(paginationRequest);
        PaginatedResponse<AddressDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByUserIdPaged(Integer userId, PaginationRequest paginationRequest) {
        Page<AddressDTO> page = addressService.getByUserIdPaged(userId, paginationRequest);
        PaginatedResponse<AddressDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchAddressesPaged(
            PaginationRequest paginationRequest,
            Integer pinCode,
            String district,
            String street,
            Integer state,
            Integer addressType,
            Integer userId) {
        Page<AddressDTO> page = addressService.searchAddressesPaged(
                paginationRequest, pinCode, district, street, state, addressType, userId
        );
        PaginatedResponse<AddressDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByCurrentUser() throws Exception {
        Integer currentUserId = auditAwareConfig.getCurrentUserId();
        List<AddressDTO> addresses = addressService.getAddressesByUserId(currentUserId);
        return CommonUtil.createBuildResponse(addresses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getCurrentUserAddressesPaged(PaginationRequest paginationRequest) {
        Integer currentUserId = auditAwareConfig.getCurrentUserId();
        Page<AddressDTO> page = addressService.getByUserIdPaged(currentUserId, paginationRequest);
        PaginatedResponse<AddressDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchAddressesForCurrentUser(Integer pinCode, String district, String street, Integer state, Integer addressType) {
        Integer currentUserId = auditAwareConfig.getCurrentUserId();
        List<AddressDTO> results = addressService.searchAddresses(pinCode, district, street, state, addressType, currentUserId);
        return CommonUtil.createBuildResponse(results, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchAddressesForCurrentUserPaged(PaginationRequest paginationRequest, Integer pinCode, String district, String street, Integer state, Integer addressType) {
        Integer currentUserId = auditAwareConfig.getCurrentUserId();
        Page<AddressDTO> page = addressService.searchAddressesPaged(paginationRequest, pinCode, district, street, state, addressType, currentUserId);
        PaginatedResponse<AddressDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }
}