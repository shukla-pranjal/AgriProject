package com.farmflow.service.impl;

import com.farmflow.dto.OrderItemDTO;
import com.farmflow.dto.OrdersDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.entity.*;
import com.farmflow.enums.OrdersStatus;
import com.farmflow.exception.InsufficientStockException;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.exception.ValidationException;
import com.farmflow.repository.*;
import com.farmflow.service.AuthService;
import com.farmflow.service.OrdersService;
import com.farmflow.service.email.EmailComposerService;
import com.farmflow.util.Constants;
import com.farmflow.util.Validation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;


@RequiredArgsConstructor
@Transactional
@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;
    public final Validation validation;
    private final EmailComposerService emailComposerService;

    @Override
    @CacheEvict(value = "orderCache", allEntries = true)
    public OrdersDTO placeOrderFromCart(Integer cartId) throws Exception {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CART, cartId)));

        if (!(authService.isOwnerOrAdmin(cart.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        if (cart.getItems().isEmpty()) {
            throw new ValidationException(Constants.CART_EMPTY);
        }

        Orders orders = new Orders();
        orders.setUser(cart.getUser());
        orders.setOrderDate(LocalDateTime.now());
        orders.setStatus(OrdersStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (!product.getAvailable()) {
                throw new ValidationException(String.format(Constants.PRODUCT_NOT_AVAILABLE, product.getName()));
            }
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException(String.format(Constants.INSUFFICIENT_STOCK, product.getName()));
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrders(orders);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            totalPrice += product.getPrice() * cartItem.getQuantity();
            orderItems.add(orderItem);
        }

        orders.setItems(orderItems);
        orders.setTotalPrice(totalPrice);


        Orders saved = orderRepository.save(orders);
        emailComposerService.sendOrderConfirmation(saved, saved.getUser());

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        cart.getItems().clear();
        cartRepository.save(cart);

        return convertToDTO(saved);
    }

    @Override
    @CacheEvict(value = "orderCache", allEntries = true)
    public OrdersDTO createManualOrder(OrdersDTO ordersDTO) throws Exception {
        validation.validateOrder(ordersDTO);

        if (ordersDTO.getUserId() == null) {
            throw new IllegalArgumentException(Constants.USER_ID_REQUIRED);
        }
        User user = userRepository.findById(ordersDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, ordersDTO.getUserId())));

        if (!(authService.isOwnerOrAdmin(user.getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        Orders orders = new Orders();
        orders.setUser(user);
        orders.setOrderDate(LocalDateTime.now());
        orders.setStatus(OrdersStatus.PENDING);

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (OrderItemDTO dto : ordersDTO.getItems()) {
            if (dto.getProductId() == null) {
                throw new IllegalArgumentException(Constants.PRODUCT_ID_REQUIRED);
            }
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, dto.getProductId())));

            if (!product.getAvailable()) {
                throw new IllegalStateException(String.format(Constants.PRODUCT_NOT_AVAILABLE, product.getName()));
            }
            if (product.getQuantity() < dto.getQuantity()) {
                throw new IllegalStateException(String.format(Constants.INSUFFICIENT_STOCK, product.getName()));
            }

            OrderItem item = new OrderItem();
            item.setOrders(orders);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPriceAtPurchase(product.getPrice());
            item.setQuantity(dto.getQuantity());

            total += product.getPrice() * dto.getQuantity();
            items.add(item);
        }

        orders.setItems(items);
        orders.setTotalPrice(total);

        Orders saved = orderRepository.save(orders);

        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, item.getProductId())));
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        return convertToDTO(saved);
    }

    @Override
    @CacheEvict(value = "orderCache", allEntries = true)
    public OrdersDTO reorder(Integer id) throws Exception {
        Orders original = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.ORDER_NOT_FOUND, id)));

        Orders copy = new Orders();
        copy.setUser(original.getUser());
        copy.setOrderDate(LocalDateTime.now());
        copy.setStatus(OrdersStatus.PENDING);

        if (!(authService.isOwnerOrAdmin(original.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        List<OrderItem> newItems = new ArrayList<>();
        double total = 0;

        for (OrderItem oldItem : original.getItems()) {
            Product product = productRepository.findById(oldItem.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, oldItem.getProductId())));

            if (!product.getAvailable()) {
                throw new IllegalStateException(String.format(Constants.PRODUCT_NOT_AVAILABLE, product.getName()));
            }
            if (product.getQuantity() < oldItem.getQuantity()) {
                throw new IllegalStateException(String.format(Constants.INSUFFICIENT_STOCK, product.getName()));
            }

            OrderItem newItem = new OrderItem();
            newItem.setOrders(copy);
            newItem.setProductId(oldItem.getProductId());
            newItem.setProductName(oldItem.getProductName());
            newItem.setPriceAtPurchase(oldItem.getPriceAtPurchase());
            newItem.setQuantity(oldItem.getQuantity());
            total += oldItem.getPriceAtPurchase() * oldItem.getQuantity();
            newItems.add(newItem);
        }

        copy.setItems(newItems);
        copy.setTotalPrice(total);

        Orders saved = orderRepository.save(copy);

        for (OrderItem item : newItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, item.getProductId())));
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        return convertToDTO(saved);
    }

    @Override
    public List<OrdersDTO> searchOrders(Integer userId, LocalDateTime fromDate, LocalDateTime toDate,
                                        OrdersStatus status, Double minTotalPrice, Double maxTotalPrice) {
        if (!(authService.isOwnerOrAdmin(userId)))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        List<Orders> orders = orderRepository.searchOrders(userId, fromDate, toDate, status, minTotalPrice, maxTotalPrice);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrdersDTO> getAllOrdersPaged(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Orders> ordersPage = orderRepository.findAll(pageable);
        return ordersPage.map(this::convertToDTO);
    }

    @Override
    public Page<OrdersDTO> getOrdersByUserPaged(Integer userId, PaginationRequest paginationRequest) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, userId)));
        if (!(authService.isOwnerOrAdmin(userId)))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        Pageable pageable = paginationRequest.toPageable();
        Page<Orders> page = orderRepository.findByUserId(userId, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<OrdersDTO> searchOrdersPaged(PaginationRequest paginationRequest,
                                             Integer userId,
                                             LocalDateTime fromDate,
                                             LocalDateTime toDate,
                                             OrdersStatus status,
                                             Double minTotalPrice,
                                             Double maxTotalPrice) {
        if (!(authService.isOwnerOrAdmin(userId)))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        Pageable pageable = paginationRequest.toPageable();
        Page<Orders> page = orderRepository.searchOrdersPaged(
                userId, fromDate, toDate, status, minTotalPrice, maxTotalPrice, pageable
        );
        return page.map(this::convertToDTO);
    }

    @Override
    @Cacheable(value = "orderCache", key = "#id")
    public OrdersDTO getOrderById(Integer id) throws Exception {
        Orders orders = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.ORDER, id)));
        if (!(authService.isOwnerOrAdmin(orders.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        return convertToDTO(orders);
    }

    @Override
    @Cacheable(value = "orderCache", key = "#userId")
    public List<OrdersDTO> getOrdersByUser(Integer userId) throws Exception {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, userId)));
        if (!(authService.isOwnerOrAdmin(userId)))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "orderCache", key = "'all'")
    public List<OrdersDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "orderCache", key = "#orderId")
    @CachePut(value = "orderCache", key = "#result.id")
    public OrdersDTO cancelOrder(Integer orderId) throws Exception {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.ORDER, orderId)));

        if (!(authService.isOwnerOrAdmin(orders.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        if (orders.getStatus() == OrdersStatus.DELIVERED) {
            throw new ValidationException(Constants.CANNOT_CANCEL_DELIVERED);
        }

        if (orders.getStatus() != OrdersStatus.CANCELLED) {
            for (OrderItem item : orders.getItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, item.getProductId())));
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        orders.setStatus(OrdersStatus.CANCELLED);
        emailComposerService.sendOrderCancellation(orders, orders.getUser());
        return convertToDTO(orderRepository.save(orders));
    }

    @Override
    @CacheEvict(value = "orderCache", key = "#orderId")
    @CachePut(value = "orderCache", key = "#result.id")
    public OrdersDTO updateOrderStatus(Integer orderId, OrdersStatus status) throws Exception {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.ORDER, orderId)));
        if (!(authService.isOwnerOrAdmin(orders.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        orders.setStatus(status);
        emailComposerService.sendStatusUpdate(orders, orders.getUser(), status );
        return convertToDTO(orderRepository.save(orders));
    }

    @Override
    @CacheEvict(value = "orderCache", key = "#orderId")
    public void deleteOrder(Integer orderId) throws Exception {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.ORDER, orderId)));
        if (!(authService.isOwnerOrAdmin(orders.getUser().getId())))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        orderRepository.delete(orders);
    }

    private OrdersDTO convertToDTO(Orders orders) {
        OrdersDTO dto = new OrdersDTO();
        dto.setId(orders.getId());
        dto.setUserId(orders.getUser().getId());
        dto.setOrderDate(orders.getOrderDate());
        dto.setStatus(orders.getStatus());
        dto.setTotalPrice(orders.getTotalPrice());

        List<OrderItemDTO> itemDTOs = orders.getItems().stream().map(item -> {
            OrderItemDTO od = new OrderItemDTO();
            od.setProductId(item.getProductId());
            od.setProductName(item.getProductName());
            od.setPriceAtPurchase(item.getPriceAtPurchase());
            od.setQuantity(item.getQuantity());
            return od;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }
}