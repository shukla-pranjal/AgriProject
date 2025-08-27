package com.farmflow.service.impl;

import com.farmflow.dto.AddressDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.entity.Address;
import com.farmflow.entity.User;
import com.farmflow.enums.AddressType;
import com.farmflow.enums.State;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.repository.AddressRepository;
import com.farmflow.repository.UserRepository;
import com.farmflow.service.AddressService;
import com.farmflow.service.AuthService;
import com.farmflow.util.Constants;
import com.farmflow.util.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    @Autowired
    private AuthService authService;

    @Override
    @Cacheable(value = "addressCache", key = "'all'")
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "addressCache", key = "#id")
    public AddressDTO getAddressById(Integer id) throws Exception {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.ADDRESS, id)));

        if (!(authService.isOwnerOrAdmin(address.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        return convertToDTO(address);
    }

    @Override
    @Cacheable(value = "addressCache", key = "#userId")
    public List<AddressDTO> getAddressesByUserId(Integer userId) throws Exception {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, userId)));

        return addressRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "addressCache", allEntries = true)
    public AddressDTO createAddress(AddressDTO addressDTO) throws Exception {
        validation.addressValidate(addressDTO);
        User user = userRepository.findById(addressDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, addressDTO.getUserId())));

        if (!(authService.isOwnerOrAdmin(addressDTO.getUserId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        return convertToDTO(savedAddress);
    }

    @Override
    @CacheEvict(value = "addressCache", key = "#id")
    @CachePut(value = "addressCache", key = "#result.id")
    public AddressDTO updateAddress(Integer id, AddressDTO addressDTO) throws Exception {
        validation.addressValidate(addressDTO);

        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.ADDRESS, id)));

        if (!(authService.isOwnerOrAdmin(addressDTO.getUserId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        modelMapper.map(addressDTO, existingAddress);
        existingAddress.setAddressType(AddressType.fromId(addressDTO.getAddressType()));
        existingAddress.setState(State.fromId(addressDTO.getState()));

        Address updatedAddress = addressRepository.save(existingAddress);
        return convertToDTO(updatedAddress);
    }

    @Override
    @CacheEvict(value = "addressCache", key = "#id")
    public void deleteAddress(Integer id) throws Exception {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.ADDRESS, id)));

        if (!(authService.isOwnerOrAdmin(address.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        addressRepository.delete(address);
    }

    @Override
    public List<AddressDTO> searchAddresses(Integer pinCode, String district, String street,
                                            Integer state, Integer addressType, Integer userId) {
        State stateEnum = state != null ? State.fromId(state) : null;
        AddressType typeEnum = addressType != null ? AddressType.fromId(addressType) : null;

        List<Address> addresses = addressRepository.searchAddresses(
                pinCode, district, street, stateEnum, typeEnum, userId
        );
        return addresses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AddressDTO> getAllPaged(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Address> page = addressRepository.findAll(pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<AddressDTO> searchAddressesPaged(PaginationRequest paginationRequest,
                                                 Integer pinCode,
                                                 String district,
                                                 String street,
                                                 Integer state,
                                                 Integer addressType,
                                                 Integer userId) {
        Pageable pageable = paginationRequest.toPageable();

        State stateEnum = state != null ? State.fromId(state) : null;
        AddressType typeEnum = addressType != null ? AddressType.fromId(addressType) : null;

        Page<Address> page = addressRepository.searchAddressesPaged(
                pinCode, district, street, stateEnum, typeEnum, userId, pageable
        );

        return page.map(this::convertToDTO);
    }

    @Override
    public Page<AddressDTO> getByUserIdPaged(Integer userId, PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Address> page = addressRepository.findByUserId(userId, pageable);
        return page.map(this::convertToDTO);
    }

    private AddressDTO convertToDTO(Address address) {
        return modelMapper.map(address, AddressDTO.class);
    }
}