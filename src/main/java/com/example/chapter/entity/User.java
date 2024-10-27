package com.example.chapter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Embedded
    private Address address;

    private boolean isDelete;

    @Column(nullable = false)
    private String provider;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    public User(String name, String password, String email, String phone, UserRoleEnum role, Address address, boolean isDelete, String provider) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.address = address;
        this.isDelete = isDelete;
        this.provider = provider;
    }

    public boolean isAdmin() {
        return this.role.equals(UserRoleEnum.ADMIN);
    }


}
