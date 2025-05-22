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
    private static final String CLASS_NAME = PaymentController.class.getSimpleName();

    @Override
    public ResponseEntity<?> createPayment(PaymentDTO paymentDTO) throws Exception {
        String methodName = "createPayment";
        log.debug("{} : {} :: Started with paymentDTO: {}", CLASS_NAME, methodName, paymentDTO);
        try {
            PaymentDTO created = paymentService.createPayment(paymentDTO);
            log.info("{} : {} :: Successfully created payment", CLASS_NAME, methodName);
            return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to create payment, error: {}", CLASS_NAME, methodName, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getPaymentById(Integer id) throws Exception {
        String methodName = "getPaymentById";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            PaymentDTO dto = paymentService.getPaymentById(id);
            log.info("{} : {} :: Successfully retrieved payment for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(dto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve payment for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getAllPayments() {
        String methodName = "getAllPayments";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);
        List<PaymentDTO> list = paymentService.getAllPayments();
        log.info("{} : {} :: Successfully retrieved {} payments", CLASS_NAME, methodName, list.size());
        return CommonUtil.createBuildResponse(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPaymentByOrderId(Integer orderId) throws Exception {
        String methodName = "getPaymentByOrderId";
        log.debug("{} : {} :: Started with orderId: {}", CLASS_NAME, methodName, orderId);
        try {
            PaymentDTO dto = paymentService.getPaymentByOrderId(orderId);
            log.info("{} : {} :: Successfully retrieved payment for orderId: {}", CLASS_NAME, methodName, orderId);
            return CommonUtil.createBuildResponse(dto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve payment for orderId: {}, error: {}", CLASS_NAME, methodName, orderId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getPaymentsByStatus(PaymentStatus status) throws Exception {
        String methodName = "getPaymentsByStatus";
        log.debug("{} : {} :: Started with status: {}", CLASS_NAME, methodName, status);
        try {
            List<PaymentDTO> list = paymentService.getPaymentsByStatus(status);
            log.info("{} : {} :: Successfully retrieved {} payments for status: {}", CLASS_NAME, methodName, list.size(), status);
            return CommonUtil.createBuildResponse(list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve payments for status: {}, error: {}", CLASS_NAME, methodName, status, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getPaymentsByMethod(PaymentMethod method) throws Exception {
        String methodName = "getPaymentsByMethod";
        log.debug("{} : {} :: Started with method: {}", CLASS_NAME, methodName, method);
        try {
            List<PaymentDTO> list = paymentService.getPaymentsByMethod(method);
            log.info("{} : {} :: Successfully retrieved {} payments for method: {}", CLASS_NAME, methodName, list.size(), method);
            return CommonUtil.createBuildResponse(list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve payments for method: {}, error: {}", CLASS_NAME, methodName, method, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> updatePayment(Integer id, PaymentDTO paymentDTO) throws Exception {
        String methodName = "updatePayment";
        log.debug("{} : {} :: Started with id: {}, paymentDTO: {}", CLASS_NAME, methodName, id, paymentDTO);
        try {
            PaymentDTO updated = paymentService.updatePayment(id, paymentDTO);
            log.info("{} : {} :: Successfully updated payment for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to update payment for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> updatePaymentStatus(Integer id, PaymentStatus status) throws Exception {
        String methodName = "updatePaymentStatus";
        log.debug("{} : {} :: Started with id: {}, status: {}", CLASS_NAME, methodName, id, status);
        try {
            PaymentDTO updated = paymentService.updatePaymentStatus(id, status);
            log.info("{} : {} :: Successfully updated payment status for id: {}, new status: {}", CLASS_NAME, methodName, id, status);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to update payment status for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> updatePaymentMethod(Integer id, PaymentMethod method) throws Exception {
        String methodName = "updatePaymentMethod";
        log.debug("{} : {} :: Started with id: {}, method: {}", CLASS_NAME, methodName, id, method);
        try {
            PaymentDTO updated = paymentService.updatePaymentMethod(id, method);
            log.info("{} : {} :: Successfully updated payment method for id: {}, new method: {}", CLASS_NAME, methodName, id, method);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to update payment method for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> deletePayment(Integer id) throws Exception {
        String methodName = "deletePayment";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            paymentService.deletePayment(id);
            log.info("{} : {} :: Successfully deleted payment for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponseMessage("Payment deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to delete payment for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchPayments(PaymentStatus status, PaymentMethod method, Double minAmount, Double maxAmount) {
        String methodName = "searchPayments";
        log.debug("{} : {} :: Started with status: {}, method: {}, minAmount: {}, maxAmount: {}", CLASS_NAME, methodName, status, method, minAmount, maxAmount);
        List<PaymentDTO> list = paymentService.searchPayments(status, method, minAmount, maxAmount);
        log.info("{} : {} :: Successfully retrieved {} payments", CLASS_NAME, methodName, list.size());
        return CommonUtil.createBuildResponse(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPaymentsPaged(PaginationRequest paginationRequest) {
        String methodName = "getAllPaymentsPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}", CLASS_NAME, methodName, paginationRequest);
        Page<PaymentDTO> page = paymentService.getAllPaymentsPaged(paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged payments, page: {}, size: {}", CLASS_NAME, methodName, page.getNumber(), page.getSize());
        PaginatedResponse<PaymentDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPaymentsByStatusPaged(PaymentStatus status, PaginationRequest paginationRequest) {
        String methodName = "getPaymentsByStatusPaged";
        log.debug("{} : {} :: Started with status: {}, paginationRequest: {}", CLASS_NAME, methodName, status, paginationRequest);
        Page<PaymentDTO> page = paymentService.getPaymentsByStatusPaged(status, paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged payments for status: {}, page: {}, size: {}", CLASS_NAME, methodName, status, page.getNumber(), page.getSize());
        PaginatedResponse<PaymentDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPaymentsByMethodPaged(PaymentMethod method, PaginationRequest paginationRequest) {
        String methodName = "getPaymentsByMethodPaged";
        log.debug("{} : {} :: Started with method: {}, paginationRequest: {}", CLASS_NAME, methodName, method, paginationRequest);
        Page<PaymentDTO> page = paymentService.getPaymentsByMethodPaged(method, paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged payments for method: {}, page: {}, size: {}", CLASS_NAME, methodName, method, page.getNumber(), page.getSize());
        PaginatedResponse<PaymentDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchPaymentsPaged(PaginationRequest paginationRequest, PaymentStatus status, PaymentMethod method, Double minAmount, Double maxAmount) {
        String methodName = "searchPaymentsPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}, status: {}, method: {}, minAmount: {}, maxAmount: {}", CLASS_NAME, methodName, paginationRequest, status, method, minAmount, maxAmount);
        Page<PaymentDTO> page = paymentService.searchPaymentsPaged(paginationRequest, status, method, minAmount, maxAmount);
        log.info("{} : {} :: Successfully retrieved paged payments, page: {}, size: {}", CLASS_NAME, methodName, page.getNumber(), page.getSize());
        PaginatedResponse<PaymentDTO> response = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }
}