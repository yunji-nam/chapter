package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import com.example.chapter.entity.User;
import lombok.Getter;

@Getter
public class ProfileDto {

    private String name;
    private String email;
    private String phone;
    private Address address;

    public ProfileDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.address = user.getAddress();
    }
}
