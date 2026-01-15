package com.farmflow.repository;

import com.farmflow.entity.Address;
import com.farmflow.enums.AddressType;
import com.farmflow.enums.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository <Address, Integer>  {
    List<Address> findByUserId(Integer userId);
    Page<Address> findByUserId(Integer userId, Pageable pageable);

    @Query("""
    SELECT a FROM Address a
    WHERE (:pinCode IS NULL OR a.pinCode = :pinCode)
      AND (:district IS NULL OR a.district LIKE %:district%)
      AND (:street IS NULL OR a.street LIKE %:street%)
      AND (:state IS NULL OR a.state = :state)
      AND (:addressType IS NULL OR a.addressType = :addressType)
      AND (:userId IS NULL OR a.user.id = :userId)
    """)
    List<Address> searchAddresses(
            @Param("pinCode") Integer pinCode,
            @Param("district") String district,
            @Param("street") String street,
            @Param("state") State state,
            @Param("addressType") AddressType addressType,
            @Param("userId") Integer userId
    );


    @Query("""
    SELECT a FROM Address a
    WHERE (:pinCode IS NULL OR a.pinCode = :pinCode)
      AND (:district IS NULL OR a.district LIKE %:district%)
      AND (:street IS NULL OR a.street LIKE %:street%)
      AND (:state IS NULL OR a.state = :state)
      AND (:addressType IS NULL OR a.addressType = :addressType)
      AND (:userId IS NULL OR a.user.id = :userId)
    """)
    Page<Address> searchAddressesPaged(
            @Param("pinCode") Integer pinCode,
            @Param("district") String district,
            @Param("street") String street,
            @Param("state") State state,
            @Param("addressType") AddressType addressType,
            @Param("userId") Integer userId,
            Pageable pageable
    );
}
