package com.agriproject.service.impl;

import com.agriproject.dto.*;
import com.agriproject.enitity.*;
import com.agriproject.exception.ResourceNotFoundException;
import com.agriproject.repository.*;
import com.agriproject.service.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public CartDTO getCartByUserId(Long userId) throws Exception {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    public CartDTO addItemToCart(Long userId, Long productId, int quantity) throws Exception {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = null;
            try {
                user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setActive(true);
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem item =modelMapper.map(product, CartItem.class
        );
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice() * quantity);
        cartItemRepository.save(item);

        cart.getItems().add(item);
        return modelMapper.map(cartRepository.save(cart), CartDTO.class);
    }

    @Override
    public void removeItemFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart(Long userId) throws Exception {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
