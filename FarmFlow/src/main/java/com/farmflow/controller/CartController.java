package com.farmflow.controller;

import com.farmflow.dto.CartDTO;
import com.farmflow.dto.CartItemDTO;
import com.farmflow.dto.PaginatedResponse;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.endpoint.CartEndpoint;
import com.farmflow.service.CartService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class CartController implements CartEndpoint {

    private final CartService cartService;

    @Override
    public ResponseEntity<?> getAll() {
        List<CartDTO> carts = cartService.getAllCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        CartDTO cartDTO = cartService.getCartById(id);
        return CommonUtil.createBuildResponse(cartDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> create(CartDTO cartDTO) throws Exception {
        CartDTO createdCart = cartService.createCart(cartDTO);
        return CommonUtil.createBuildResponse(createdCart, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> update(Integer id, CartDTO cartDTO) throws Exception {

        CartDTO updatedCart = cartService.updateCart(id, cartDTO);
        return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        cartService.deleteCart(id);
        return CommonUtil.createBuildResponseMessage("Cart deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addItem(Integer id, CartItemDTO cartItemDTO) throws Exception {
        CartDTO updatedCart = cartService.addItem(id, cartItemDTO);
        return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateItemQuantity(Integer id, Integer productId, CartItemDTO cartItemDTO) throws Exception {
        CartDTO updatedCart = cartService.updateItemQuantity(id, productId, cartItemDTO.getQuantity());
        return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeItem(Integer id, Integer productId) throws Exception {
        CartDTO updatedCart = cartService.removeItem(id, productId);
        return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> increaseItemQuantity(Integer id, Integer productId, Integer amount) throws Exception {
        CartDTO updatedCart = cartService.increaseItemQuantity(id, productId, amount);
        return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> decreaseItemQuantity(Integer id, Integer productId, Integer amount) throws Exception {
        CartDTO updatedCart = cartService.decreaseItemQuantity(id, productId, amount);
        return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchItemsInCart(Integer id, String productName, Integer minQuantity) throws Exception {
        List<CartItemDTO> results = cartService.searchCartItems(id, productName, minQuantity);
        return CommonUtil.createBuildResponse(results, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllCartsPaged(PaginationRequest paginationRequest) {
        Page<CartDTO> page = cartService.getAllCartsPaged(paginationRequest);
        PaginatedResponse<CartDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchItemsInCartPaged(Integer id, PaginationRequest paginationRequest, String productName, Integer minQuantity) throws Exception {
        Page<CartItemDTO> page = cartService.searchCartItemsPaged(id, paginationRequest, productName, minQuantity);
        PaginatedResponse<CartItemDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }
}