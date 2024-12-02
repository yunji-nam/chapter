package com.example.chapter.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDto {
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9~!@#$%^&*()+=;?]{4,15}$", message = "비밀번호는 4자 이상 15자 미만으로 입력해 주세요.")
    private String currentPassword;
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9~!@#$%^&*()+=;?]{4,15}$", message = "비밀번호는 4자 이상 15자 미만으로 입력해 주세요.")
    private String newPassword;
}
