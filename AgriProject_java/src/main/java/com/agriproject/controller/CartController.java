package com.agriproject.controller;

import com.agriproject.dto.CartDTO;
import com.agriproject.service.CartService;
import com.agriproject.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable Long userId) throws Exception {
        CartDTO cart = cartService.getCartByUserId(userId);
        return CommonUtil.createBuildResponse(cart, HttpStatus.OK);
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<?> addItemToCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) throws Exception {

        CartDTO updatedCart = cartService.addItemToCart(userId, productId, quantity);
        return CommonUtil.createBuildResponse(updatedCart, HttpStatus.CREATED);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long cartItemId) {
        cartService.removeItemFromCart(cartItemId);
        return CommonUtil.createBuildResponseMessage("Cart item removed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) throws Exception {
        cartService.clearCart(userId);
        return CommonUtil.createBuildResponseMessage("Cart cleared successfully", HttpStatus.OK);
    }
}
