package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProfileDto {

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9~!@#$%^&*()+=;?]{8,15}$", message = "비밀번호는 8자 이상 15자 미만으로 입력해 주세요.")
    private String password;
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-z]{2,}$", message = "올바른 이메일 주소를 입력해 주세요.")
    private String email;
    @Pattern(regexp = "^010[0-9]{8}$", message = "올바른 휴대폰 번호를 입력해 주세요.")
    private String phone;
    private Address address;
}
