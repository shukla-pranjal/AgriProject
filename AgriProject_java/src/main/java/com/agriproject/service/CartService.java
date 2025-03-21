package com.agriproject.service;

import com.agriproject.dto.CartDTO;

public interface CartService {
    CartDTO getCartByUserId(Long userId);
    CartDTO addItemToCart(Long userId, Long productId, int quantity);
    void removeItemFromCart(Long cartItemId);
    void clearCart(Long userId);
}
