package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileDto {

    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-z]{2,}$", message = "올바른 이메일 주소를 입력해 주세요.")
    private String email;
    @Pattern(regexp = "^010[0-9]{8}$", message = "올바른 휴대폰 번호를 입력해 주세요.")
    private String phone;
    private Address address;

    public UpdateProfileDto(ProfileDto dto) {
        this.email = dto.getEmail();
        this.phone = dto.getPhone();
        this.address = dto.getAddress();
    }
}
