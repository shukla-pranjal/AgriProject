package com.agriproject.service;

import com.agriproject.dto.CartDTO;

public interface CartService {
    CartDTO getCartByUserId(Long userId) throws Exception;
    CartDTO addItemToCart(Long userId, Long productId, int quantity) throws Exception;
    void removeItemFromCart(Long cartItemId);
    void clearCart(Long userId) throws Exception;
}
