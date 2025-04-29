package com.agriproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agriproject.enitity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
