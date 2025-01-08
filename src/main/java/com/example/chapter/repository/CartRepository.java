package com.example.chapter.repository;

import com.example.chapter.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);

    @Query("select coalesce(sum(ci.quantity), 0) from CartItem  ci where ci.cart.user.id = :userId")
    Integer getCartItemQuantityByUserId(Long userId);
}
