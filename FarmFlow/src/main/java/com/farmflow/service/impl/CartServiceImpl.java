package com.farmflow.service.impl;

import com.farmflow.dto.CartDTO;
import com.farmflow.dto.CartItemDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.entity.Cart;
import com.farmflow.entity.CartItem;
import com.farmflow.entity.Product;
import com.farmflow.entity.User;
import com.farmflow.exception.DuplicateResourceException;
import com.farmflow.exception.InsufficientStockException;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.exception.ValidationException;
import com.farmflow.repository.CartRepository;
import com.farmflow.repository.ProductRepository;
import com.farmflow.repository.UserRepository;
import com.farmflow.service.AuthService;
import com.farmflow.service.CartService;
import com.farmflow.service.email.EmailComposerService;
import com.farmflow.util.Constants;
import com.farmflow.util.Validation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final Validation validation;
    private final AuthService authService;
    private final EmailComposerService emailComposerService;

    private void validateProductAvailability(Product product, int requestedQuantity) throws Exception {
        if (!product.getAvailable()) {
            throw new ResourceNotFoundException(String.format(Constants.PRODUCT_NOT_AVAILABLE, product.getId()));
        }
        if (product.getQuantity() < requestedQuantity) {
            emailComposerService.sendLowStockAlert(product, product.getFarmer());
            throw new InsufficientStockException(String.format(Constants.INSUFFICIENT_STOCK, product.getId()));
        }
    }

    @Override
    @Cacheable(value = "cartCache", key = "#id")
    public CartDTO getCartById(Integer id) throws Exception {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CART, id)));

        if (!(authService.isOwnerOrAdmin(cart.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        return convertToDTO(cart);
    }

    @Override
    @CacheEvict(value = "cartCache", allEntries = true)
    public CartDTO createCart(CartDTO cartDTO) throws Exception {
        if (cartDTO.getUserId() == null) {
            throw new ValidationException(Constants.USER_ID_REQUIRED);
        }

        User user = userRepository.findById(cartDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, cartDTO.getUserId())));

        if (!(authService.isOwnerOrAdmin(user.getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        Optional<Cart> existingCart = cartRepository.findByUserId(cartDTO.getUserId());
        if (existingCart.isPresent()) {
            throw new DuplicateResourceException(String.format(Constants.USER_HAS_CART, cartDTO.getUserId()));
        }

        Cart cart = new Cart();
        cart.setUser(user);

        List<CartItem> items = new ArrayList<>();
        if (cartDTO.getItems() != null) {
            for (CartItemDTO itemDTO : cartDTO.getItems()) {
                validation.cartItemValidate(itemDTO);

                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, itemDTO.getProductId())));

                validateProductAvailability(product, itemDTO.getQuantity());

                CartItem item = new CartItem();
                item.setCart(cart);
                item.setProduct(product);
                item.setQuantity(itemDTO.getQuantity());
                items.add(item);
            }
        }

        cart.setItems(items);
        return convertToDTO(cartRepository.save(cart));
    }

    @Override
    @CacheEvict(value = "cartCache", key = "#id")
    @CachePut(value = "cartCache", key = "#result.id")
    public CartDTO updateCart(Integer id, CartDTO cartDTO) throws Exception {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CART, id)));

        if (cartDTO.getUserId() != null && !cartDTO.getUserId().equals(cart.getUser().getId())) {
            User user = userRepository.findById(cartDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, cartDTO.getUserId())));

            if (!(authService.isOwnerOrAdmin(user.getId())))
                throw new AccessDeniedException(Constants.ACCESS_DENIED);

            cart.setUser(user);
        }

        if (cartDTO.getItems() != null) {
            cart.getItems().clear();

            for (CartItemDTO itemDTO : cartDTO.getItems()) {
                validation.cartItemValidate(itemDTO);

                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, itemDTO.getProductId())));

                validateProductAvailability(product, itemDTO.getQuantity());

                CartItem item = new CartItem();
                item.setCart(cart);
                item.setProduct(product);
                item.setQuantity(itemDTO.getQuantity());
                cart.getItems().add(item);
            }
        }

        return convertToDTO(cartRepository.save(cart));
    }

    @Override
    @CacheEvict(value = "cartCache", key = "#id")
    public void deleteCart(Integer id) throws Exception {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CART, id)));
        if (!(authService.isOwnerOrAdmin(cart.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        cartRepository.delete(cart);
    }

    @Override
    @CacheEvict(value = "cartCache", key = "#cartId")
    public CartDTO addItem(Integer cartId, CartItemDTO cartItemDTO) throws Exception {
        validation.cartItemValidate(cartItemDTO);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CART, cartId)));

        if (!(authService.isOwnerOrAdmin(cart.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, cartItemDTO.getProductId())));

        validateProductAvailability(product, cartItemDTO.getQuantity());

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + cartItemDTO.getQuantity();
            validateProductAvailability(product, newQuantity);
            existingItem.setQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(cartItemDTO.getQuantity());
            cart.getItems().add(newItem);
        }

        return convertToDTO(cartRepository.save(cart));
    }

    @Override
    @CacheEvict(value = "cartCache", key = "#cartId")
    public CartDTO updateItemQuantity(Integer cartId, Integer productId, Integer quantity) throws Exception {
        if (quantity == null || quantity < 1) {
            throw new ValidationException(Constants.QUANTITY_MUST_BE_POSITIVE);
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CART, cartId)));

        if (!(authService.isOwnerOrAdmin(cart.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.PRODUCT_NOT_IN_CART, productId)));

        validateProductAvailability(item.getProduct(), quantity);
        item.setQuantity(quantity);

        return convertToDTO(cartRepository.save(cart));
    }

    @Override
    @CacheEvict(value = "cartCache", key = "#cartId")
    public CartDTO removeItem(Integer cartId, Integer productId) throws Exception {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CART, cartId)));

        if (!(authService.isOwnerOrAdmin(cart.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        boolean removed = cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));

        if (!removed) {
            throw new ResourceNotFoundException(String.format(Constants.PRODUCT_NOT_IN_CART, productId));
        }

        return convertToDTO(cartRepository.save(cart));
    }

    @Override
    @CacheEvict(value = "cartCache", key = "#cartId")
    public CartDTO increaseItemQuantity(Integer cartId, Integer productId, Integer amount) throws Exception {
        if (amount == null || amount < 1) {
            throw new ValidationException(Constants.AMOUNT_MUST_BE_POSITIVE);
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CART, cartId)));

        if (!(authService.isOwnerOrAdmin(cart.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.PRODUCT_NOT_IN_CART, productId)));

        int newQuantity = item.getQuantity() + amount;
        validateProductAvailability(item.getProduct(), newQuantity);
        item.setQuantity(newQuantity);

        return convertToDTO(cartRepository.save(cart));
    }

    @Override
    @CacheEvict(value = "cartCache", key = "#cartId")
    public CartDTO decreaseItemQuantity(Integer cartId, Integer productId, Integer amount) throws Exception {
        if (amount == null || amount < 1) {
            throw new ValidationException(Constants.AMOUNT_MUST_BE_POSITIVE);
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CART, cartId)));

        if (!(authService.isOwnerOrAdmin(cart.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.PRODUCT_NOT_IN_CART, productId)));

        int newQuantity = item.getQuantity() - amount;

        if (newQuantity < 1) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(newQuantity);
        }

        return convertToDTO(cartRepository.save(cart));
    }

    @Override
    @Cacheable(value = "cartCache", key = "'all'")
    public List<CartDTO> getAllCarts() {
        return cartRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartItemDTO> searchCartItems(Integer id, String productName, Integer minQuantity) throws Exception {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CART, id)));
        Stream<CartItem> filteredStream = cart.getItems().stream();

        if (!(authService.isOwnerOrAdmin(cart.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        if (productName != null && !productName.isEmpty()) {
            filteredStream = filteredStream.filter(item ->
                    item.getProduct().getName().toLowerCase().contains(productName.toLowerCase())
            );
        }

        if (minQuantity != null) {
            filteredStream = filteredStream.filter(item -> item.getQuantity() >= minQuantity);
        }

        return filteredStream
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CartItemDTO> searchCartItemsPaged(Integer cartId, PaginationRequest paginationRequest, String productName, Integer minQuantity) throws Exception {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CART, cartId)));

        if (!(authService.isOwnerOrAdmin(cart.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        Stream<CartItem> filteredStream = cart.getItems().stream();

        if (productName != null && !productName.isEmpty()) {
            filteredStream = filteredStream.filter(item ->
                    item.getProduct().getName().toLowerCase().contains(productName.toLowerCase())
            );
        }

        if (minQuantity != null) {
            filteredStream = filteredStream.filter(item -> item.getQuantity() >= minQuantity);
        }

        List<CartItemDTO> filteredList = filteredStream
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        int start = paginationRequest.getPage() * paginationRequest.getSize();
        int end = Math.min(start + paginationRequest.getSize(), filteredList.size());

        if (start > filteredList.size()) {
            start = 0;
            end = 0;
        }

        List<CartItemDTO> pagedList = filteredList.subList(start, end);

        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                paginationRequest.isAscending() ?
                        Sort.by(paginationRequest.getSortBy()).ascending() :
                        Sort.by(paginationRequest.getSortBy()).descending()
        );

        return new PageImpl<>(pagedList, pageable, filteredList.size());
    }

    @Override
    public Page<CartDTO> getAllCartsPaged(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        return cartRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    private CartItemDTO convertToDTO(CartItem item) {
        return CartItemDTO.builder()
                .productId(item.getProduct().getId())
                .quantity(item.getQuantity())
                .build();
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUser().getId());

        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> {
                    CartItemDTO itemDTO = new CartItemDTO();
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setQuantity(item.getQuantity());
                    return itemDTO;
                }).collect(Collectors.toList());

        cartDTO.setItems(itemDTOs);
        return cartDTO;
    }
}