package com.agriproject.service.impl;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agriproject.dto.AddressDTO;
import com.agriproject.enitity.Address;
import com.agriproject.exception.ResourceNotFoundException;
import com.agriproject.repository.AddressRepository;
import com.agriproject.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();

        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO getAddressById(Long id) throws Exception {
        Address address = addressRepository.findById(id).orElseThrow(() -> new  ResourceNotFoundException   ("Address not found"));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = modelMapper.map(addressDTO, Address.class);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) throws Exception {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found: " + id));
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
        
    }

    @Override
    public void deleteAddress(Long id)throws Exception {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found: " + id));
        addressRepository.delete(address);

    }
    
}