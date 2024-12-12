package com.example.chapter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount; // 결제 금액
    private boolean isPaid; // 결제 여부
    private LocalDateTime paidAt; // 결제 날짜
    private String paymentMethod; // 결제 방식

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    public PaymentEntity(BigDecimal amount, String paymentMethod, Order order) {
        this.amount = amount;
        this.isPaid = true;
        this.paidAt = LocalDateTime.now();
        this.paymentMethod = paymentMethod;
        this.order = order;
    }

}
