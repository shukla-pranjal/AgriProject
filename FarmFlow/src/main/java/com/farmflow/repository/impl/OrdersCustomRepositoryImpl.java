package com.farmflow.repository.impl;

import com.farmflow.entity.Orders;
import com.farmflow.enums.OrdersStatus;
import com.farmflow.repository.OrdersCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrdersCustomRepositoryImpl implements OrdersCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Orders> searchOrders(Integer userId, LocalDateTime fromDate, LocalDateTime toDate,
                                     OrdersStatus status, Double minTotalPrice, Double maxTotalPrice) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> root = cq.from(Orders.class);

        List<Predicate> predicates = new ArrayList<>();

        if (userId != null) {
            predicates.add(cb.equal(root.get("user").get("id"), userId));
        }

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("orderDate"), fromDate));
        }

        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("orderDate"), toDate));
        }

        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        if (minTotalPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("totalPrice"), minTotalPrice));
        }

        if (maxTotalPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("totalPrice"), maxTotalPrice));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("orderDate")));

        return entityManager.createQuery(cq).getResultList();
    }
}
