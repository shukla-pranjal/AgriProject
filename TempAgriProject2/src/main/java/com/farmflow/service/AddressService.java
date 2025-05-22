package com.farmflow.service;

import com.farmflow.dto.AddressDTO;
import com.farmflow.dto.PaginationRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Integer id)  throws Exception ;

    List<AddressDTO> getAddressesByUserId(Integer userId)throws Exception;

    AddressDTO createAddress(AddressDTO addressDTO) throws Exception;

    AddressDTO updateAddress(Integer id, AddressDTO addressDTO)  throws Exception ;

    void deleteAddress(Integer id)  throws Exception ;

    List<AddressDTO> searchAddresses(Integer pinCode, String district, String street, Integer state, Integer addressType, Integer userId);

    Page<AddressDTO> getAllPaged(PaginationRequest paginationRequest);

    Page<AddressDTO> searchAddressesPaged(PaginationRequest paginationRequest, Integer pinCode, String district, String street, Integer state, Integer addressType, Integer userId);

    Page<AddressDTO> getByUserIdPaged(Integer userId, PaginationRequest paginationRequest);
}
