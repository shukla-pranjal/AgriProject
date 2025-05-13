/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.agriproject.service;

import java.util.List;

import com.agriproject.dto.AddressDTO;

public interface  AddressService {

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long id) throws Exception;

    AddressDTO createAddress(AddressDTO addressDTO);

    AddressDTO updateAddress(Long id, AddressDTO addressDTO) throws Exception;

    void deleteAddress(Long id) throws Exception;

}
