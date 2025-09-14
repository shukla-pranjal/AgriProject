package com.farmflow.controller;

import com.farmflow.dto.PaginatedResponse;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.PaymentDTO;
import com.farmflow.endpoint.PaymentEndpoint;
import com.farmflow.enums.PaymentMethod;
import com.farmflow.enums.PaymentStatus;
import com.farmflow.service.PaymentService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController implements PaymentEndpoint {

    private final PaymentService paymentService;

    @Override
    public ResponseEntity<?> createPayment(PaymentDTO paymentDTO) throws Exception {
            PaymentDTO created = paymentService.createPayment(paymentDTO);
            return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> getPaymentById(Integer id) throws Exception {
            PaymentDTO dto = paymentService.getPaymentById(id);
            return CommonUtil.createBuildResponse(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPayments() {
        List<PaymentDTO> list = paymentService.getAllPayments();
        return CommonUtil.createBuildResponse(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPaymentByOrderId(Integer orderId) throws Exception {
            PaymentDTO dto = paymentService.getPaymentByOrderId(orderId);
            return CommonUtil.createBuildResponse(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPaymentsByStatus(PaymentStatus status) throws Exception {
            List<PaymentDTO> list = paymentService.getPaymentsByStatus(status);
            return CommonUtil.createBuildResponse(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPaymentsByMethod(PaymentMethod method) throws Exception {
            List<PaymentDTO> list = paymentService.getPaymentsByMethod(method);
            return CommonUtil.createBuildResponse(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updatePayment(Integer id, PaymentDTO paymentDTO) throws Exception {
            PaymentDTO updated = paymentService.updatePayment(id, paymentDTO);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updatePaymentStatus(Integer id, PaymentStatus status) throws Exception {
            PaymentDTO updated = paymentService.updatePaymentStatus(id, status);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updatePaymentMethod(Integer id, PaymentMethod method) throws Exception {
            PaymentDTO updated = paymentService.updatePaymentMethod(id, method);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deletePayment(Integer id) throws Exception {
            paymentService.deletePayment(id);
            return CommonUtil.createBuildResponseMessage("Payment deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchPayments(PaymentStatus status, PaymentMethod method, Double minAmount, Double maxAmount) {
        List<PaymentDTO> list = paymentService.searchPayments(status, method, minAmount, maxAmount);
        return CommonUtil.createBuildResponse(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPaymentsPaged(PaginationRequest paginationRequest) {
        Page<PaymentDTO> page = paymentService.getAllPaymentsPaged(paginationRequest);
        PaginatedResponse<PaymentDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPaymentsByStatusPaged(PaymentStatus status, PaginationRequest paginationRequest) {
        Page<PaymentDTO> page = paymentService.getPaymentsByStatusPaged(status, paginationRequest);
        PaginatedResponse<PaymentDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPaymentsByMethodPaged(PaymentMethod method, PaginationRequest paginationRequest) {
        Page<PaymentDTO> page = paymentService.getPaymentsByMethodPaged(method, paginationRequest);
        PaginatedResponse<PaymentDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchPaymentsPaged(PaginationRequest paginationRequest, PaymentStatus status, PaymentMethod method, Double minAmount, Double maxAmount) {
        Page<PaymentDTO> page = paymentService.searchPaymentsPaged(paginationRequest, status, method, minAmount, maxAmount);
        PaginatedResponse<PaymentDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }
}