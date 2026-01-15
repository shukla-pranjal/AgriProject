package com.farmflow.service;

import com.farmflow.dto.ProductDTO;
import com.farmflow.entity.Product;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.repository.ProductRepository;
import com.farmflow.service.impl.ProductServiceImpl;
import com.farmflow.util.Constants;
import com.farmflow.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setCreatedBy(100);

        productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setName("Test Product");
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenAuthorized() throws Exception {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(authService.isOwnerOrAdmin(100)).thenReturn(true);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        ProductDTO result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getProductById_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(1);
        });

        String expectedMessage = String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, 1);
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
