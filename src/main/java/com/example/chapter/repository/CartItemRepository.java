package com.example.chapter.repository;

import com.example.chapter.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Modifying
    @Query("delete from CartItem c where c.id in :cartItemIds")
    void deleteByIdIn(List<Long> cartItemIds);
}
