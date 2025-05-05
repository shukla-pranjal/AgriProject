package com.agriproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agriproject.enitity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
