package com.example.chapter.dto;

import com.example.chapter.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserInfo {
    private Long id;
    private String email;
    private String username;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;
    private String role;
    private boolean deleted;

    public UserInfo(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getName();
        this.phone = user.getPhone();
        this.joinDate = user.getCreatedAt().toLocalDate();
        this.role = user.getRole().toString();
        this.deleted = user.isDeleted();
    }

    public String getMaskedPhone() {
        if (phone == null) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

}
