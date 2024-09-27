package com.example.chapter.service;

import com.example.chapter.dto.SignUpDto;
import com.example.chapter.entity.Address;
import com.example.chapter.entity.User;
import com.example.chapter.entity.UserRoleEnum;
import com.example.chapter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    /*
     * 회원가입
     */
    public void join(SignUpDto signUpDto) {
        String name = signUpDto.getName();
        String password = passwordEncoder.encode(signUpDto.getPassword());
        String email = signUpDto.getEmail();
        String phone = signUpDto.getPhone();
        String city = signUpDto.getAddress().getCity();
        String street = signUpDto.getAddress().getStreet();
        String zipcode = signUpDto.getAddress().getZipcode();

        UserRoleEnum role = UserRoleEnum.USER;

        if (checkName(name)){
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }

        if (checkEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if (checkPhone(phone)) {
            throw new IllegalArgumentException("이미 존재하는 휴대폰 번호입니다.");
        }

        User user = new User(name, password, email, phone, role,
                new Address(city, street, zipcode), false, "chapter");

        userRepository.save(user);
    }

    private boolean checkName(String name) {
        return userRepository.existsByName(name);
    }
    private boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    private boolean checkPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

}
