package com.farmflow.repository;

import com.farmflow.entity.User;
import com.farmflow.enums.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmailIgnoreCase(String email);

    @Query("""
    SELECT u FROM User u
    WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
      AND (:phone IS NULL OR u.phone LIKE CONCAT('%', :phone, '%'))
""")
    List<User> searchUsers(
            @Param("name") String name,
            @Param("email") String email,
            @Param("phone") String phone
    );

    @Query("""
    SELECT u FROM User u
    WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
      AND (:phone IS NULL OR u.phone LIKE CONCAT('%', :phone, '%'))
""")
    Page<User> searchUsersPaged(@Param("name") String name,
                                @Param("email") String email,
                                @Param("phone") String phone,
                                Pageable pageable);

    Optional<User> findByImageId(Integer imageId);

    User findByEmailIgnoreCase(String email);

    List<User> findAllByMetadataStatusAndMetadataVerificationCodeCreatedAtBefore(AccountStatus status, LocalDateTime dateTime);

    Optional<User> findByEmail(String email);
}


