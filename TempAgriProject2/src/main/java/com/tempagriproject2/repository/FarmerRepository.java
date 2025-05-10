package com.tempagriproject2.repository;

import com.tempagriproject2.entity.Farmer;
import com.tempagriproject2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Integer> {

    Optional<Farmer> findByUser(User user);
    Optional<Farmer> findByUserId(Integer userId);

}
