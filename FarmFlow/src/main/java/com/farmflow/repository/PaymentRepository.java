package com.farmflow.repository;

import com.farmflow.entity.Payment;
import com.farmflow.enums.PaymentMethod;
import com.farmflow.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByOrdersId(Integer orderId);
    List<Payment> findByPaymentStatus(PaymentStatus status);
    List<Payment> findByPaymentMethod(PaymentMethod method);
    @Query("""
    SELECT p FROM Payment p
    WHERE (:status IS NULL OR p.paymentStatus = :status)
      AND (:method IS NULL OR p.paymentMethod = :method)
      AND (:minAmount IS NULL OR p.amount >= :minAmount)
      AND (:maxAmount IS NULL OR p.amount <= :maxAmount)
""")
    List<Payment> search(
            @Param("status") PaymentStatus status,
            @Param("method") PaymentMethod method,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount
    );


    Page<Payment> findByPaymentMethod(PaymentMethod method, Pageable pageable);
    Page<Payment> findByPaymentStatus(PaymentStatus status, Pageable pageable);

    @Query("""
    SELECT p FROM Payment p
    WHERE (:status IS NULL OR p.paymentStatus = :status)
      AND (:method IS NULL OR p.paymentMethod = :method)
      AND (:minAmount IS NULL OR p.amount >= :minAmount)
      AND (:maxAmount IS NULL OR p.amount <= :maxAmount)
""")
    Page<Payment> searchPaged(
            @Param("status") PaymentStatus status,
            @Param("method") PaymentMethod method,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            Pageable pageable
    );

}
