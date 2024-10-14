package com.example.chapter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    @NotEmpty
    private String email;

    @NotEmpty
    private String phone;

    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Embedded
    private Address address;

    private boolean isDelete;

    @NotEmpty
    private String provider;

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
