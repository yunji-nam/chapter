package com.example.chapter.repository;

import com.example.chapter.entity.DeliveryStatus;
import com.example.chapter.entity.Order;
import com.example.chapter.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserIdAndOrderDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Optional<Order> findByMerchantUid(String merchantUid);

    boolean existsByIdAndUserIdAndItemsIdAndStatusAndDeliveryStatus(Long orderId, Long userId, Long orderItemId, OrderStatus orderStatus, DeliveryStatus deliveryStatus);
}
