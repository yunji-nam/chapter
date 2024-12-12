package com.example.chapter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String merchantUid;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    public Order(String merchantUid, User user, Delivery delivery, List<OrderItem> items) {
        this.merchantUid = merchantUid;
        this.user = user;
        this.orderDate = LocalDateTime.now();
        this.delivery = delivery;
        this.status = OrderStatus.PAID;
        this.items = items;
    }

    public void addOrderItem(Book book, int quantity) {
        OrderItem orderItem = new OrderItem(this, book, quantity);
        items.add(orderItem);
    }

    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
            throw new IllegalStateException("이미 배송이 완료된 상품입니다.");
        }

        this.status = OrderStatus.CANCEL;
        for (OrderItem orderItem : items) {
            orderItem.cancel();
        }
    }

}
