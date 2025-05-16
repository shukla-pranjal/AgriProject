package com.tempagriproject2.service;

import com.tempagriproject2.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Integer id)  throws Exception ;

    List<AddressDTO> getAddressesByUserId(Integer userId)throws Exception;

    AddressDTO createAddress(AddressDTO addressDTO) throws Exception;

    AddressDTO updateAddress(Integer id, AddressDTO addressDTO)  throws Exception ;

    void deleteAddress(Integer id)  throws Exception ;
}
