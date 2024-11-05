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

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public Delivery(Address address, String name, String phone) {
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.status = DeliveryStatus.READY;
    }

}