package com.farmflow.service.impl;

import com.farmflow.dto.FarmerDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.entity.Farmer;
import com.farmflow.entity.User;
import com.farmflow.enums.Role;
import com.farmflow.exception.DuplicateResourceException;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.exception.ValidationException;
import com.farmflow.repository.FarmerRepository;
import com.farmflow.repository.UserRepository;
import com.farmflow.service.AuthService;
import com.farmflow.service.FarmerService;
import com.farmflow.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Validation validation;
    private final AuthService authService;

    @Override
    public List<FarmerDTO> getAllFarmers() {
        return farmerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FarmerDTO getFarmerById(Integer id) throws Exception {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + id));

        if (!(authService.isOwnerOrAdmin(farmer.getUser().getId())))
            throw new AccessDeniedException("Access denied");

        return convertToDTO(farmer);
    }

    @Override
    public FarmerDTO createFarmer(FarmerDTO farmerDTO) throws Exception {
        try {
            validation.farmerValidate(farmerDTO);
        } catch (Exception ex) {
            throw new ValidationException("Invalid farmer data: " + ex.getMessage());
        }

        User user = userRepository.findById(farmerDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + farmerDTO.getUserId()));

        if (!(authService.isOwnerOrAdmin(user.getId())))
            throw new AccessDeniedException("Access denied");

        if (farmerRepository.findByUserId(user.getId()).isPresent()) {
            throw new DuplicateResourceException("User with ID " + user.getId() + " is already registered as a Farmer.");
        }

        Farmer farmer = modelMapper.map(farmerDTO, Farmer.class);
        farmer.setUser(user);

        Set<Role> roles = user.getRoles();
        roles.add(Role.ROLE_FARMER);

        userRepository.save(user);

        Farmer savedFarmer = farmerRepository.save(farmer);
        return convertToDTO(savedFarmer);
    }

    @Override
    public FarmerDTO updateFarmer(Integer id, FarmerDTO farmerDTO) throws Exception {
        try {
            validation.farmerValidate(farmerDTO);
        } catch (Exception ex) {
            throw new ValidationException("Invalid farmer data: " + ex.getMessage());
        }

        Farmer existingFarmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + id));

        modelMapper.map(farmerDTO, existingFarmer);
        Farmer updatedFarmer = farmerRepository.save(existingFarmer);
        return convertToDTO(updatedFarmer);
    }

    @Override
    public void deleteFarmer(Integer id) throws Exception {
        Farmer existingFarmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + id));

        User user = existingFarmer.getUser();

        if (!(authService.isOwnerOrAdmin(user.getId())))
            throw new AccessDeniedException("Access denied");

        Set<Role> roles = user.getRoles();
        if (roles != null && roles.contains(Role.ROLE_FARMER)) {
            roles.remove(Role.ROLE_FARMER);
            user.setRoles(roles);
            userRepository.save(user);
        }

        farmerRepository.delete(existingFarmer);
    }

    @Override
    public boolean isUserFarmer(Integer userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        if (!(authService.isOwnerOrAdmin(user.getId())))
            throw new AccessDeniedException("Access denied");
        return farmerRepository.findByUserId(userId).isPresent();
    }

    @Override
    public List<FarmerDTO> searchFarmers(String farmName, String farmType, String locationDescription, String governmentId) {
        List<Farmer> farmers = farmerRepository.searchFarmers(farmName, farmType, locationDescription, governmentId);
        return farmers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FarmerDTO> searchFarmersPaged(PaginationRequest paginationRequest, String farmName, String farmType, String locationDescription, String governmentId) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Farmer> page = farmerRepository.searchFarmersPaged(farmName, farmType, locationDescription, governmentId, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<FarmerDTO> getAllFarmersPaged(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        return farmerRepository.findAll(pageable).map(this::convertToDTO);
    }

    private FarmerDTO convertToDTO(Farmer farmer) {
        return modelMapper.map(farmer, FarmerDTO.class);
    }
}
