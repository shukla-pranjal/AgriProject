package com.agriproject.repository;

import com.agriproject.enitity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository extends JpaRepository<Cart, Long> {

}
