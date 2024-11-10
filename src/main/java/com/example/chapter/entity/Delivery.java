package com.example.chapter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    private String name;

    private String phone;

    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public Delivery(Address address, String name, String phone) {
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.status = DeliveryStatus.READY;
    }

    public void addTrackingNumber(String trackingNumber) {
        if (this.trackingNumber == null) {
            this.trackingNumber = trackingNumber;
            this.status = DeliveryStatus.SHIPPING;
        } else {
            throw new IllegalStateException("운송장 번호가 이미 등록되어 있습니다.");
        }
    }

}