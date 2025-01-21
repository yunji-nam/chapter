package com.example.chapter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends CreatedTimeStamped {

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

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    public Order(String merchantUid, User user, Delivery delivery, List<OrderItem> items) {
        this.merchantUid = merchantUid;
        this.user = user;
        this.delivery = delivery;
        this.status = OrderStatus.PAID;
        this.items = items;
    }

    public void addOrderItem(Book book, int quantity) {
        OrderItem orderItem = new OrderItem(this, book, quantity);
        items.add(orderItem);
    }

    public void cancel() {
        if (this.status == OrderStatus.CANCEL) {
            throw new IllegalStateException("취소된 주문입니다.");
        }
        if (delivery.getStatus() != null) {
            throw new IllegalStateException("배송상태에 따라 취소가 불가능합니다.");
        }
        this.status = OrderStatus.CANCEL;
        for (OrderItem orderItem : items) {
            orderItem.cancel();
        }
    }

}
