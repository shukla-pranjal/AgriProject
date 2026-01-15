package com.farmflow.repository.impl;

import com.farmflow.entity.Farmer;
import com.farmflow.repository.FarmerCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FarmerCustomRepositoryImpl implements FarmerCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Farmer> searchFarmers(String farmName, String farmType,
                                      String locationDiscription, String governmentId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Farmer> cq = cb.createQuery(Farmer.class);
        Root<Farmer> root = cq.from(Farmer.class);

        List<Predicate> predicates = new ArrayList<>();


        if (farmName != null && !farmName.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("farmName")), "%" + farmName.toLowerCase() + "%"));
        }

        if (farmType != null && !farmType.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("farmType")), "%" + farmType.toLowerCase() + "%"));
        }

        if (locationDiscription != null && !locationDiscription.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("locationDiscription")), "%" + locationDiscription.toLowerCase() + "%"));
        }

        if (governmentId != null && !governmentId.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("governmentId")), "%" + governmentId.toLowerCase() + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }

}
