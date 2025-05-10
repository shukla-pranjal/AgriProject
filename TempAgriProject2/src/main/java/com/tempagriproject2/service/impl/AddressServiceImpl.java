package com.tempagriproject2.service.impl;

import com.tempagriproject2.dto.AddressDTO;
import com.tempagriproject2.entity.Address;
import com.tempagriproject2.entity.User;
import com.tempagriproject2.enums.AddressType;
import com.tempagriproject2.enums.State;
import com.tempagriproject2.exception.ResourceNotFoundException;
import com.tempagriproject2.repository.AddressRepository;
import com.tempagriproject2.repository.UserRepository;
import com.tempagriproject2.service.AddressService;
import com.tempagriproject2.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validation validation;

    @Override
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(Integer id) throws Exception {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + id));
        return convertToDTO(address);
    }



    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) throws Exception {
        // Check if the user exists
        User user = userRepository.findById(addressDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + addressDTO.getUserId()));

        // Proceed with mapping and saving the address
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user); // Set the actual user entity

        Address savedAddress = addressRepository.save(address);
        return convertToDTO(savedAddress);
    }


    @Override
    public AddressDTO updateAddress(Integer id, AddressDTO addressDTO) throws Exception {
        validation.addressValidate(addressDTO);

        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + id));

        modelMapper.map(addressDTO, existingAddress);

        // Updated AddressType and State handling
        existingAddress.setAddressType(AddressType.fromId(addressDTO.getAddressType()));
        existingAddress.setState(State.fromId(addressDTO.getState())); // Use fromId() for State

        Address updatedAddress = addressRepository.save(existingAddress);
        return convertToDTO(updatedAddress);
    }


    @Override
    public void deleteAddress(Integer id) throws Exception {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + id));
        addressRepository.delete(address);
    }

    private AddressDTO convertToDTO(Address address) {
        return modelMapper.map(address, AddressDTO.class);
    }
}
