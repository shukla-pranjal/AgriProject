package com.tempagriproject2.controller;

import com.tempagriproject2.dto.AddressDTO;
import com.tempagriproject2.endpoint.AddressEndpoint;
import com.tempagriproject2.service.AddressService;
import com.tempagriproject2.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController implements AddressEndpoint {

    private final AddressService addressService;

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
    public ResponseEntity<?> create(AddressDTO addressDTO)throws Exception {
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
}
