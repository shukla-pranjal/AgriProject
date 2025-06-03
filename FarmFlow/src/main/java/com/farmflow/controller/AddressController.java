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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AddressController implements AddressEndpoint {

    private final AddressService addressService;
    private final AuditAwareConfig auditAwareConfig;
    private static final String CLASS_NAME = AddressController.class.getSimpleName();

    @Override
    public ResponseEntity<?> getAll() {
        String methodName = "getAll";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);
        List<AddressDTO> addressList = addressService.getAllAddresses();
        log.info("{} : {} :: Successfully retrieved {} addresses", CLASS_NAME, methodName, addressList.size());
        return CommonUtil.createBuildResponse(addressList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        String methodName = "getById";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            AddressDTO address = addressService.getAddressById(id);
            log.info("{} : {} :: Successfully retrieved address for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(address, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve address for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> create(AddressDTO addressDTO) throws Exception {
        String methodName = "create";
        log.debug("{} : {} :: Started with addressDTO: {}", CLASS_NAME, methodName, addressDTO);
        try {
            AddressDTO created = addressService.createAddress(addressDTO);
            log.info("{} : {} :: Successfully created address", CLASS_NAME, methodName);
            return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to create address, error: {}", CLASS_NAME, methodName, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> update(Integer id, AddressDTO addressDTO) throws Exception {
        String methodName = "update";
        log.debug("{} : {} :: Started with id: {}, addressDTO: {}", CLASS_NAME, methodName, id, addressDTO);
        try {
            AddressDTO updated = addressService.updateAddress(id, addressDTO);
            log.info("{} : {} :: Successfully updated address for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to update address for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        String methodName = "delete";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            addressService.deleteAddress(id);
            log.info("{} : {} :: Successfully deleted address for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponseMessage("Address deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to delete address for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getByUserId(Integer userId) throws Exception {
        String methodName = "getByUserId";
        log.debug("{} : {} :: Started with userId: {}", CLASS_NAME, methodName, userId);
        try {
            List<AddressDTO> addresses = addressService.getAddressesByUserId(userId);
            log.info("{} : {} :: Successfully retrieved {} addresses for userId: {}", CLASS_NAME, methodName, addresses.size(), userId);
            return CommonUtil.createBuildResponse(addresses, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve addresses for userId: {}, error: {}", CLASS_NAME, methodName, userId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchAddresses(Integer pinCode, String district, String street, Integer state, Integer addressType, Integer userId) {
        String methodName = "searchAddresses";
        log.debug("{} : {} :: Started with pinCode: {}, district: {}, street: {}, state: {}, addressType: {}, userId: {}",
                CLASS_NAME, methodName, pinCode, district, street, state, addressType, userId);
        List<AddressDTO> results = addressService.searchAddresses(pinCode, district, street, state, addressType, userId);
        log.info("{} : {} :: Successfully retrieved {} addresses", CLASS_NAME, methodName, results.size());
        return CommonUtil.createBuildResponse(results, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPaged(PaginationRequest paginationRequest) {
        String methodName = "getAllPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}", CLASS_NAME, methodName, paginationRequest);
        Page<AddressDTO> page = addressService.getAllPaged(paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged addresses, page: {}, size: {}",
                CLASS_NAME, methodName, page.getNumber(), page.getSize());
        PaginatedResponse<AddressDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByUserIdPaged(Integer userId, PaginationRequest paginationRequest) {
        String methodName = "getByUserIdPaged";
        log.debug("{} : {} :: Started with userId: {}, paginationRequest: {}", CLASS_NAME, methodName, userId, paginationRequest);
        Page<AddressDTO> page = addressService.getByUserIdPaged(userId, paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged addresses for userId: {}, page: {}, size: {}",
                CLASS_NAME, methodName, userId, page.getNumber(), page.getSize());
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
        String methodName = "searchAddressesPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}, pinCode: {}, district: {}, street: {}, state: {}, addressType: {}, userId: {}",
                CLASS_NAME, methodName, paginationRequest, pinCode, district, street, state, addressType, userId);
        Page<AddressDTO> page = addressService.searchAddressesPaged(
                paginationRequest, pinCode, district, street, state, addressType, userId
        );
        log.info("{} : {} :: Successfully retrieved paged addresses, page: {}, size: {}",
                CLASS_NAME, methodName, page.getNumber(), page.getSize());
        PaginatedResponse<AddressDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByCurrentUser() throws Exception {
        String methodName = "getByCurrentUser";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);
        try {
            Integer currentUserId = auditAwareConfig.getCurrentUserId();
            List<AddressDTO> addresses = addressService.getAddressesByUserId(currentUserId);
            log.info("{} : {} :: Successfully retrieved {} addresses for current user", CLASS_NAME, methodName, addresses.size());
            return CommonUtil.createBuildResponse(addresses, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve addresses for current user, error: {}", CLASS_NAME, methodName, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getCurrentUserAddressesPaged(PaginationRequest paginationRequest) throws Exception {
        String methodName = "getCurrentUserAddressesPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}", CLASS_NAME, methodName, paginationRequest);
        try {
            Integer currentUserId = auditAwareConfig.getCurrentUserId();
            Page<AddressDTO> page = addressService.getByUserIdPaged(currentUserId, paginationRequest);
            log.info("{} : {} :: Successfully retrieved paged addresses for current user, page: {}, size: {}",
                    CLASS_NAME, methodName, page.getNumber(), page.getSize());
            PaginatedResponse<AddressDTO> paginatedResponse = PaginatedResponse.fromPage(page);
            return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve paged addresses for current user, error: {}", CLASS_NAME, methodName, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchAddressesForCurrentUser(Integer pinCode, String district, String street, Integer state, Integer addressType) {
        String methodName = "searchAddressesForCurrentUser";
        log.debug("{} : {} :: Started with pinCode: {}, district: {}, street: {}, state: {}, addressType: {}",
                CLASS_NAME, methodName, pinCode, district, street, state, addressType);
        try {
            Integer currentUserId = auditAwareConfig.getCurrentUserId();
            List<AddressDTO> results = addressService.searchAddresses(pinCode, district, street, state, addressType, currentUserId);
            log.info("{} : {} :: Successfully retrieved {} addresses for current user", CLASS_NAME, methodName, results.size());
            return CommonUtil.createBuildResponse(results, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to search addresses for current user, error: {}", CLASS_NAME, methodName, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchAddressesForCurrentUserPaged(PaginationRequest paginationRequest, Integer pinCode, String district, String street, Integer state, Integer addressType) {
        String methodName = "searchAddressesForCurrentUserPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}, pinCode: {}, district: {}, street: {}, state: {}, addressType: {}",
                CLASS_NAME, methodName, paginationRequest, pinCode, district, street, state, addressType);
        try {
            Integer currentUserId = auditAwareConfig.getCurrentUserId();
            Page<AddressDTO> page = addressService.searchAddressesPaged(paginationRequest, pinCode, district, street, state, addressType, currentUserId);
            log.info("{} : {} :: Successfully retrieved paged addresses for current user, page: {}, size: {}",
                    CLASS_NAME, methodName, page.getNumber(), page.getSize());
            PaginatedResponse<AddressDTO> paginatedResponse = PaginatedResponse.fromPage(page);
            return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to search paged addresses for current user, error: {}", CLASS_NAME, methodName, e.getMessage());
            throw e;
        }
    }
}
