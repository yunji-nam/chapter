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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Embedded
    private Address address;

    private boolean isDelete;

    @Column(nullable = false)
    private String provider;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    public User(String name, String password, String email, String phone, UserRoleEnum role, Address address, String provider) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.address = address;
        this.isDelete = false;
        this.provider = provider;
    }

    public User(String name, String password, String email, UserRoleEnum role, String provider) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role =role;
        this.isDelete = false;
        this.provider = provider;
    }

    public void updateUser(String password, String email, String phone, Address address) {
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public boolean isAdmin() {
        return this.role.equals(UserRoleEnum.ADMIN);
    }


}
