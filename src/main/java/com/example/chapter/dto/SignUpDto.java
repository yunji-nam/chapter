package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {

    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "이름은 한글, 영문 대/소문자를 사용해 주세요.")
    private String name;
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9~!@#$%^&*()+=;?]{6,15}$", message = "비밀번호는 6자 이상 15자 미만으로 입력해 주세요.")
    private String password;
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-z]{2,}$", message = "올바른 이메일 주소를 입력해 주세요.")
    private String email;
    @Pattern(regexp = "^010[0-9]{8}$", message = "올바른 휴대폰 번호를 입력해 주세요.")
    private String phone;
    private Address address;

}
