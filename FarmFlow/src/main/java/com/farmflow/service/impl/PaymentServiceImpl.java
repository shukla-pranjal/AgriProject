package com.farmflow.service.impl;

import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.PaymentDTO;
import com.farmflow.entity.Orders;
import com.farmflow.entity.Payment;
import com.farmflow.enums.OrdersStatus;
import com.farmflow.enums.PaymentMethod;
import com.farmflow.enums.PaymentStatus;
import com.farmflow.exception.DuplicateResourceException;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.exception.ValidationException;
import com.farmflow.repository.PaymentRepository;
import com.farmflow.repository.OrdersRepository;
import com.farmflow.service.AuthService;
import com.farmflow.service.PaymentService;
import com.farmflow.service.email.EmailComposerService;
import com.farmflow.util.Constants;
import com.farmflow.util.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final AuthService authService;
    private final Validation validation;
    private final EmailComposerService emailComposerService;

    @Override
    @CacheEvict(value = "paymentCache", allEntries = true)
    public PaymentDTO createPayment(PaymentDTO paymentDTO) throws Exception {
        validation.paymentValidate(paymentDTO);

        Optional<Payment> existingPayment = paymentRepository.findByOrdersId(paymentDTO.getOrdersId());
        if (existingPayment.isPresent()) {
            throw new DuplicateResourceException(String.format(Constants.PAYMENT_EXISTS_FOR_ORDER, paymentDTO.getOrdersId()));
        }

        Orders order = ordersRepository.findById(paymentDTO.getOrdersId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.ORDER, paymentDTO.getOrdersId())));

        if (authService.isOwnerOrAdmin(order.getUser().getId()))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        if (order.getStatus() == OrdersStatus.CANCELLED) {
            throw new ValidationException(String.format(Constants.PAYMENT_CANCELLED_ORDER, order.getId()));
        }

        Payment payment = toEntity(paymentDTO);
        Payment savedPayment = paymentRepository.save(payment);

        syncOrderStatusAfterPayment(savedPayment);

        emailComposerService.sendPaymentReceipt(paymentDTO, payment.getOrders().getUser());

        return toDTO(savedPayment);
    }

    @Override
    @Cacheable(value = "paymentCache", key = "#id")
    public PaymentDTO getPaymentById(Integer id) throws Exception {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PAYMENT, id)));

        if (authService.isOwnerOrAdmin(payment.getCreatedBy()))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        return toDTO(payment);
    }

    @Override
    @Cacheable(value = "paymentCache", key = "'all'")
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "paymentCache", key = "#orderId")
    public PaymentDTO getPaymentByOrderId(Integer orderId) throws Exception {
        Payment payment = paymentRepository.findByOrdersId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.PAYMENT_NOT_FOUND_FOR_ORDER, orderId)));
        if (authService.isOwnerOrAdmin(payment.getCreatedBy()))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        return toDTO(payment);
    }

    @Override
    @Cacheable(value = "paymentCache", key = "#status.name()")
    public List<PaymentDTO> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "paymentCache", key = "#method.name()")
    public List<PaymentDTO> getPaymentsByMethod(PaymentMethod method) {
        return paymentRepository.findByPaymentMethod(method).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "paymentCache", key = "#id")
    @CachePut(value = "paymentCache", key = "#result.id")
    public PaymentDTO updatePayment(Integer id, PaymentDTO paymentDTO) throws Exception { // TODO
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PAYMENT, id)));

        if (authService.isOwnerOrAdmin(payment.getCreatedBy()))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        validation.paymentValidate(paymentDTO);

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED &&
                paymentDTO.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new ValidationException(Constants.CANNOT_DOWNGRADE_COMPLETED);
        }

        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());

        Payment updatedPayment = paymentRepository.save(payment);
        syncOrderStatusAfterPayment(updatedPayment);

        emailComposerService.sendPaymentUpdateNotification(updatedPayment, paymentDTO);

        return toDTO(updatedPayment);
    }

    @Override
    @CacheEvict(value = "paymentCache", key = "#id")
    @CachePut(value = "paymentCache", key = "#result.id")
    public PaymentDTO updatePaymentStatus(Integer id, PaymentStatus status) throws Exception {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PAYMENT, id)));

        if (authService.isOwnerOrAdmin(payment.getCreatedBy()))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED && status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException(Constants.CANNOT_DOWNGRADE_COMPLETED);
        }

        payment.setPaymentStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);

        syncOrderStatusAfterPayment(updatedPayment);

        return toDTO(updatedPayment);
    }

    @Override
    @CacheEvict(value = "paymentCache", key = "#id")
    @CachePut(value = "paymentCache", key = "#result.id")
    public PaymentDTO updatePaymentMethod(Integer id, PaymentMethod method) throws Exception {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PAYMENT, id)));
        if (authService.isOwnerOrAdmin(payment.getCreatedBy()))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        payment.setPaymentMethod(method);

        emailComposerService.sendPaymentMethodUpdated(payment, method);
        return toDTO(paymentRepository.save(payment));
    }

    @Override
    @CacheEvict(value = "paymentCache", key = "#id")
    public void deletePayment(Integer id) throws Exception {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PAYMENT, id)));
        if (authService.isOwnerOrAdmin(payment.getCreatedBy()))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            Orders order = payment.getOrders();
            if (order != null) {
                order.setStatus(OrdersStatus.PENDING);
                ordersRepository.save(order);
            }
        }

        paymentRepository.delete(payment);
    }

    @Override
    public List<PaymentDTO> searchPayments(PaymentStatus status, PaymentMethod method, Double minAmount, Double maxAmount) {
        return paymentRepository.search(status, method, minAmount, maxAmount).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PaymentDTO> getPaymentsByMethodPaged(PaymentMethod method, PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Payment> page = paymentRepository.findByPaymentMethod(method, pageable);
        return page.map(this::toDTO);
    }

    @Override
    public Page<PaymentDTO> getPaymentsByStatusPaged(PaymentStatus status, PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Payment> page = paymentRepository.findByPaymentStatus(status, pageable);
        return page.map(this::toDTO);
    }

    @Override
    public Page<PaymentDTO> getAllPaymentsPaged(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Payment> page = paymentRepository.findAll(pageable);
        return page.map(this::toDTO);
    }

    @Override
    public Page<PaymentDTO> searchPaymentsPaged(PaginationRequest paginationRequest,
                                                PaymentStatus status,
                                                PaymentMethod method,
                                                Double minAmount,
                                                Double maxAmount) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Payment> page = paymentRepository.searchPaged(status, method, minAmount, maxAmount, pageable);
        return page.map(this::toDTO);
    }

    private PaymentDTO toDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setTransactionId(payment.getTransactionId());
        dto.setAmount(payment.getAmount());
        dto.setOrdersId(payment.getOrders() != null ? payment.getOrders().getId() : null);
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setPaymentMethod(payment.getPaymentMethod());
        return dto;
    }

    private Payment toEntity(PaymentDTO dto) throws Exception {
        Payment payment = new Payment();
        payment.setTransactionId(dto.getTransactionId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setPaymentMethod(dto.getPaymentMethod());

        if (dto.getOrdersId() != null) {
            Orders order = ordersRepository.findById(dto.getOrdersId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.ORDER, dto.getOrdersId())));
            payment.setOrders(order);
        } else {
            throw new ValidationException(Constants.ORDER_ID_REQUIRED);
        }
        return payment;
    }

    private void syncOrderStatusAfterPayment(Payment payment) {
        Orders order = payment.getOrders();
        if (order == null) return;

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED && order.getStatus() == OrdersStatus.PENDING) {
            order.setStatus(OrdersStatus.PAID);
            ordersRepository.save(order);
        }
    }
}