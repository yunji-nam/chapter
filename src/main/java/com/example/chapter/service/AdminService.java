package com.example.chapter.service;

import com.example.chapter.dto.SignUpDto;
import com.example.chapter.entity.User;
import com.example.chapter.entity.UserRoleEnum;
import com.example.chapter.exception.DuplicateFieldException;
import com.example.chapter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //관리자 회원 가입
    @Transactional
    public void join(SignUpDto signUpDto) {
        String name = signUpDto.getName();
        String password = passwordEncoder.encode(signUpDto.getPassword());
        String email = signUpDto.getEmail();
        String phone = signUpDto.getPhone();

        UserRoleEnum role = UserRoleEnum.ADMIN;

        User user = User.builder()
                        .name(name)
                        .password(password)
                        .email(validateEmail(email))
                        .phone(validatePhone(phone))
                        .role(role)
                        .isDelete(false)
                        .provider("chapter").build();

        userRepository.save(user);
    }

    private String validateEmail(String email) {
        if (userRepository.existsByEmail(email)) throw new DuplicateFieldException("이메일", email);
        return email;
    }

    private String validatePhone(String phone) {
        if (userRepository.existsByPhone(phone)) throw new DuplicateFieldException("휴대폰 번호", phone);
        return phone;
    }
}
