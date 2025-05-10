package com.tempagriproject2.service.impl;

import com.tempagriproject2.dto.FarmerDTO;
import com.tempagriproject2.entity.Farmer;
import com.tempagriproject2.entity.User;
import com.tempagriproject2.exception.ResourceNotFoundException;
import com.tempagriproject2.repository.FarmerRepository;
import com.tempagriproject2.repository.UserRepository;
import com.tempagriproject2.service.FarmerService;
import com.tempagriproject2.util.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerServiceImpl implements FarmerService {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validation validation;

    @Override
    public List<FarmerDTO> getAllFarmers() {
        return farmerRepository.findAll().stream()
                .map(farmer -> modelMapper.map(farmer, FarmerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public FarmerDTO getFarmerById(Integer id) throws Exception {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + id));
        return modelMapper.map(farmer, FarmerDTO.class);
    }

    @Override
    public FarmerDTO createFarmer(FarmerDTO farmerDTO) throws Exception {
        validation.farmerValidate(farmerDTO);
        User  user = userRepository.findById(farmerDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + farmerDTO.getUserId()));
        Optional<Farmer> existingFarmer = farmerRepository.findByUserId(user.getId());
        if (existingFarmer.isPresent()) {
            throw new ResourceNotFoundException("User with ID " + user.getId() + " is already registered as a Farmer.");
        }
        Farmer farmer = modelMapper.map(farmerDTO, Farmer.class);
        farmer.setUser(user);

        Farmer savedFarmer = farmerRepository.save(farmer);
        return modelMapper.map(savedFarmer, FarmerDTO.class);
    }

    @Override
    public FarmerDTO updateFarmer(Integer id, FarmerDTO farmerDTO) throws Exception {
        validation.farmerValidate(farmerDTO);
        Farmer existingFarmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + id));

        modelMapper.map(farmerDTO, existingFarmer);
        Farmer updatedFarmer = farmerRepository.save(existingFarmer);
        return modelMapper.map(updatedFarmer, FarmerDTO.class);
    }

    @Override
    public void deleteFarmer(Integer id) throws Exception {
        Farmer existingFarmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + id));
        farmerRepository.delete(existingFarmer);
    }
    @Override
    public boolean isUserFarmer(Integer userId) throws Exception {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return farmerRepository.findByUserId(userId).isPresent();
    }


}