package com.farmflow.repository;

import com.farmflow.entity.Orders;
import com.farmflow.enums.OrdersStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer>, OrdersCustomRepository {
    List<Orders> findByUserId(Integer userId);

    Page<Orders> findByUserId(Integer userId, Pageable pageable);

    @Query("""
    SELECT o FROM Orders o
    WHERE (:userId IS NULL OR o.user.id = :userId)
      AND (:fromDate IS NULL OR o.orderDate >= :fromDate)
      AND (:toDate IS NULL OR o.orderDate <= :toDate)
      AND (:status IS NULL OR o.status = :status)
      AND (:minTotalPrice IS NULL OR o.totalPrice >= :minTotalPrice)
      AND (:maxTotalPrice IS NULL OR o.totalPrice <= :maxTotalPrice)
""")
    Page<Orders> searchOrdersPaged(
            @Param("userId") Integer userId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("status") OrdersStatus status,
            @Param("minTotalPrice") Double minTotalPrice,
            @Param("maxTotalPrice") Double maxTotalPrice,
            Pageable pageable
    );

}
