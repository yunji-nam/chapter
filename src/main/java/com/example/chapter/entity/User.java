package com.example.chapter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
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

    public void update(String email, String phone, Address address) {
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public boolean isAdmin() {
        return this.role.equals(UserRoleEnum.ADMIN);
    }


}
