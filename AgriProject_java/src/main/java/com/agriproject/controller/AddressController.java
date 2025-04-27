package com.agriproject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agriproject.dto.AddressDTO;
import com.agriproject.service.AddressService;
import com.agriproject.util.CommonUtil;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {
    
    private final AddressService addressService;
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<AddressDTO> addresses = addressService.getAllAddresses();
        return CommonUtil.createBuildResponse(addresses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id)  throws Exception{
        AddressDTO address = addressService.getAddressById(id);
        return CommonUtil.createBuildResponse(address, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AddressDTO addressDTO) {
        AddressDTO created = addressService.createAddress(addressDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AddressDTO addressDTO) throws Exception {
        AddressDTO updated = addressService.updateAddress(id, addressDTO);
        return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception{
        addressService.deleteAddress(id);
        return CommonUtil.createBuildResponseMessage("Address deleted successfully", HttpStatus.OK);
    }
}
