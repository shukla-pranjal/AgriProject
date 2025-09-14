package com.farmflow.service;

import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.PaymentDTO;
import com.farmflow.enums.PaymentMethod;
import com.farmflow.enums.PaymentStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentService {

    PaymentDTO createPayment(PaymentDTO paymentDTO) throws Exception;

    PaymentDTO getPaymentById(Integer id) throws Exception;

    List<PaymentDTO> getAllPayments();

    PaymentDTO getPaymentByOrderId(Integer orderId) throws Exception;

    List<PaymentDTO> getPaymentsByStatus(PaymentStatus status) throws Exception;

    List<PaymentDTO> getPaymentsByMethod(PaymentMethod method) throws Exception;

    PaymentDTO updatePayment(Integer id, PaymentDTO paymentDTO) throws Exception;

    PaymentDTO updatePaymentStatus(Integer id, PaymentStatus status) throws Exception;

    PaymentDTO updatePaymentMethod(Integer id, PaymentMethod method) throws Exception;

    void deletePayment(Integer id) throws Exception;

    List<PaymentDTO> searchPayments(PaymentStatus status, PaymentMethod method, Double minAmount, Double maxAmount);

    Page<PaymentDTO> getPaymentsByMethodPaged(PaymentMethod method, PaginationRequest paginationRequest);

    Page<PaymentDTO> getPaymentsByStatusPaged(PaymentStatus status, PaginationRequest paginationRequest);

    Page<PaymentDTO> getAllPaymentsPaged(PaginationRequest paginationRequest);

    Page<PaymentDTO> searchPaymentsPaged(PaginationRequest paginationRequest, PaymentStatus status, PaymentMethod method, Double minAmount, Double maxAmount);
}
