package com.farmflow.config;

import com.farmflow.dto.*;
import com.farmflow.entity.*;
import com.farmflow.enums.AddressType;
import com.farmflow.enums.State;
import com.farmflow.enums.Unit;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class ProjectConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Skip ID when mapping UserDTO to User
        modelMapper.typeMap(UserDTO.class, User.class)
                .addMappings(mapper -> mapper.skip(User::setId));

        // Skip ID when mapping AddressDTO to Address
        modelMapper.typeMap(AddressDTO.class, Address.class)
                .addMappings(mapper -> mapper.skip(Address::setId));

        modelMapper.typeMap(CategoryDTO.class, Category.class)
                .addMappings(mapper -> mapper.skip(Category::setId));

        // Ensure Address ID is copied correctly to DTO (AddressDTO)
        modelMapper.typeMap(Address.class, AddressDTO.class)
                .addMappings(mapper -> mapper.map(Address::getId, AddressDTO::setId));

        // Skip ID when mapping FarmerDTO to Farmer
        modelMapper.typeMap(FarmerDTO.class, Farmer.class)
                .addMappings(mapper -> mapper.skip(Farmer::setId));

        // Custom mapping for AddressType to handle conversion based on ID
        modelMapper.typeMap(AddressDTO.class, Address.class)
                .addMappings(mapper -> mapper.using(ctx -> {
                    Integer id = (Integer) ctx.getSource();
                    return AddressType.fromId(id); // Convert ID to AddressType enum
                }).map(AddressDTO::getAddressType, Address::setAddressType));

        // Custom mapping for State to handle conversion based on ID
        modelMapper.typeMap(AddressDTO.class, Address.class)
                .addMappings(mapper -> mapper.using(ctx -> {
                    Integer id = (Integer) ctx.getSource();
                    return State.fromId(id); // Convert ID to State enum
                }).map(AddressDTO::getState, Address::setState));

        // Custom mapping for AddressType (when mapping from Address to AddressDTO)
        modelMapper.typeMap(Address.class, AddressDTO.class)
                .addMappings(mapper -> mapper.using(ctx -> ((AddressType) ctx.getSource()).getId())  // Convert AddressType enum to its ID (Integer)
                        .map(Address::getAddressType, AddressDTO::setAddressType));

        // Custom mapping for State (when mapping from Address to AddressDTO)
        modelMapper.typeMap(Address.class, AddressDTO.class)
                .addMappings(mapper -> mapper.using(ctx -> ((State) ctx.getSource()).getId()) // Convert State enum to its ID (Integer)
                        .map(Address::getState, AddressDTO::setState));
        // Mapping from ProductDTO to Product
        modelMapper.typeMap(ProductDTO.class, Product.class)
                .addMappings(mapper -> {
                    mapper.skip(Product::setId);
                    mapper.using(ctx -> Unit.fromId((Integer) ctx.getSource()))
                            .map(ProductDTO::getUnit, Product::setUnit);
                });

        // ✅ Mapping from Product to ProductDTO (fix for your error)
        modelMapper.typeMap(Product.class, ProductDTO.class)
                .addMappings(mapper -> mapper.using(ctx -> ((Unit) ctx.getSource()).getId())
                        .map(Product::getUnit, ProductDTO::setUnit));

        // Skip ID when mapping ReviewDTO to Review
        modelMapper.typeMap(ReviewDTO.class, Review.class)
                .addMappings(mapper -> mapper.skip(Review::setId));


        return modelMapper;
    }




    @Bean
    AuditorAware<Integer> auditorAware() {
        return new AuditAwareConfig();
    }
}
