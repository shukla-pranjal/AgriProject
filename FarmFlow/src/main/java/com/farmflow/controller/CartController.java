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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CartController implements CartEndpoint {

    private final CartService cartService;
    private static final String CLASS_NAME = CartController.class.getSimpleName();

    @Override
    public ResponseEntity<?> getAll() {
        String methodName = "getAll";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);
        List<CartDTO> carts = cartService.getAllCarts();
        log.info("{} : {} :: Successfully retrieved {} carts", CLASS_NAME, methodName, carts.size());
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        String methodName = "getById";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            CartDTO cartDTO = cartService.getCartById(id);
            log.info("{} : {} :: Successfully retrieved cart for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(cartDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve cart for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> create(CartDTO cartDTO) throws Exception {
        String methodName = "create";
        log.debug("{} : {} :: Started with cartDTO: {}", CLASS_NAME, methodName, cartDTO);
        try {
            CartDTO createdCart = cartService.createCart(cartDTO);
            log.info("{} : {} :: Successfully created cart", CLASS_NAME, methodName);
            return CommonUtil.createBuildResponse(createdCart, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to create cart, error: {}", CLASS_NAME, methodName, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> update(Integer id, CartDTO cartDTO) throws Exception {
        String methodName = "update";
        log.debug("{} : {} :: Started with id: {}, cartDTO: {}", CLASS_NAME, methodName, id, cartDTO);
        try {
            CartDTO updatedCart = cartService.updateCart(id, cartDTO);
            log.info("{} : {} :: Successfully updated cart for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to update cart for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        String methodName = "delete";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            cartService.deleteCart(id);
            log.info("{} : {} :: Successfully deleted cart for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponseMessage("Cart deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to delete cart for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> addItem(Integer id, CartItemDTO cartItemDTO) throws Exception {
        String methodName = "addItem";
        log.debug("{} : {} :: Started with id: {}, cartItemDTO: {}", CLASS_NAME, methodName, id, cartItemDTO);
        try {
            CartDTO updatedCart = cartService.addItem(id, cartItemDTO);
            log.info("{} : {} :: Successfully added item to cart for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to add item to cart for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> updateItemQuantity(Integer id, Integer productId, CartItemDTO cartItemDTO) throws Exception {
        String methodName = "updateItemQuantity";
        log.debug("{} : {} :: Started with id: {}, productId: {}, quantity: {}", CLASS_NAME, methodName, id, productId, cartItemDTO.getQuantity());
        try {
            CartDTO updatedCart = cartService.updateItemQuantity(id, productId, cartItemDTO.getQuantity());
            log.info("{} : {} :: Successfully updated item quantity for cart id: {}, productId: {}", CLASS_NAME, methodName, id, productId);
            return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to update item quantity for cart id: {}, productId: {}, error: {}", CLASS_NAME, methodName, id, productId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> removeItem(Integer id, Integer productId) throws Exception {
        String methodName = "removeItem";
        log.debug("{} : {} :: Started with id: {}, productId: {}", CLASS_NAME, methodName, id, productId);
        try {
            CartDTO updatedCart = cartService.removeItem(id, productId);
            log.info("{} : {} :: Successfully removed item from cart for id: {}, productId: {}", CLASS_NAME, methodName, id, productId);
            return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to remove item from cart for id: {}, productId: {}, error: {}", CLASS_NAME, methodName, id, productId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> increaseItemQuantity(Integer id, Integer productId, Integer amount) throws Exception {
        String methodName = "increaseItemQuantity";
        log.debug("{} : {} :: Started with id: {}, productId: {}, amount: {}", CLASS_NAME, methodName, id, productId, amount);
        try {
            CartDTO updatedCart = cartService.increaseItemQuantity(id, productId, amount);
            log.info("{} : {} :: Successfully increased item quantity for cart id: {}, productId: {}", CLASS_NAME, methodName, id, productId);
            return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to increase item quantity for cart id: {}, productId: {}, error: {}", CLASS_NAME, methodName, id, productId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> decreaseItemQuantity(Integer id, Integer productId, Integer amount) throws Exception {
        String methodName = "decreaseItemQuantity";
        log.debug("{} : {} :: Started with id: {}, productId: {}, amount: {}", CLASS_NAME, methodName, id, productId, amount);
        try {
            CartDTO updatedCart = cartService.decreaseItemQuantity(id, productId, amount);
            log.info("{} : {} :: Successfully decreased item quantity for cart id: {}, productId: {}", CLASS_NAME, methodName, id, productId);
            return CommonUtil.createBuildResponse(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to decrease item quantity for cart id: {}, productId: {}, error: {}", CLASS_NAME, methodName, id, productId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchItemsInCart(Integer id, String productName, Integer minQuantity) throws Exception {
        String methodName = "searchItemsInCart";
        log.debug("{} : {} :: Started with id: {}, productName: {}, minQuantity: {}", CLASS_NAME, methodName, id, productName, minQuantity);
        try {
            List<CartItemDTO> results = cartService.searchCartItems(id, productName, minQuantity);
            log.info("{} : {} :: Successfully retrieved {} items for cart id: {}", CLASS_NAME, methodName, results.size(), id);
            return CommonUtil.createBuildResponse(results, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to search items in cart for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getAllCartsPaged(PaginationRequest paginationRequest) {
        String methodName = "getAllCartsPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}", CLASS_NAME, methodName, paginationRequest);
        Page<CartDTO> page = cartService.getAllCartsPaged(paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged carts, page: {}, size: {}", CLASS_NAME, methodName, page.getNumber(), page.getSize());
        PaginatedResponse<CartDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchItemsInCartPaged(Integer id, PaginationRequest paginationRequest, String productName, Integer minQuantity) throws Exception {
        String methodName = "searchItemsInCartPaged";
        log.debug("{} : {} :: Started with id: {}, paginationRequest: {}, productName: {}, minQuantity: {}", CLASS_NAME, methodName, id, paginationRequest, productName, minQuantity);
        try {
            Page<CartItemDTO> page = cartService.searchCartItemsPaged(id, paginationRequest, productName, minQuantity);
            log.info("{} : {} :: Successfully retrieved paged items for cart id: {}, page: {}, size: {}", CLASS_NAME, methodName, id, page.getNumber(), page.getSize());
            PaginatedResponse<CartItemDTO> paginatedResponse = PaginatedResponse.fromPage(page);
            return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to search paged items in cart for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }
}