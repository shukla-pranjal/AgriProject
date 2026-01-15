package com.farmflow.service;

import com.farmflow.dto.CartDTO;
import com.farmflow.dto.CartItemDTO;
import com.farmflow.dto.PaginationRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CartService {

    CartDTO getCartById(Integer id) throws Exception;

    CartDTO createCart(CartDTO cartDTO) throws Exception;

    CartDTO updateCart(Integer id, CartDTO cartDTO) throws Exception;

    void deleteCart(Integer id) throws Exception;

    CartDTO addItem(Integer cartId, CartItemDTO cartItemDTO) throws Exception;

    CartDTO updateItemQuantity(Integer cartId, Integer productId, Integer quantity) throws Exception;

    CartDTO removeItem(Integer cartId, Integer productId) throws Exception;

    CartDTO increaseItemQuantity(Integer cartId, Integer productId, Integer amount) throws Exception;

    CartDTO decreaseItemQuantity(Integer cartId, Integer productId, Integer amount) throws Exception;

    List<CartDTO> getAllCarts();

    List<CartItemDTO> searchCartItems(Integer id, String productName, Integer minQuantity) throws Exception;

    Page<CartItemDTO> searchCartItemsPaged(Integer id, PaginationRequest paginationRequest, String productName, Integer minQuantity)throws Exception;

    Page<CartDTO> getAllCartsPaged(PaginationRequest paginationRequest);
}
