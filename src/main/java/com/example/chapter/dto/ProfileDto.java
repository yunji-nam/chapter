package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import com.example.chapter.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {

    private String username;
    private String email;
    private String phone;
    private Address address;

    public ProfileDto(User user) {
        this.username = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.address = user.getAddress();
    }

    public String getFormatPhoneNumber() {
        if (phone == null) {
            return "";
        }
        return phone.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
    }
}
