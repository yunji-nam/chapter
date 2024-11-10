package com.example.chapter.service;

import com.example.chapter.dto.ProfileDto;
import com.example.chapter.dto.SignUpDto;
import com.example.chapter.entity.Address;
import com.example.chapter.entity.User;
import com.example.chapter.entity.UserRoleEnum;
import com.example.chapter.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

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


    /**
     * 회원가입
     * @param signUpDto
     */
    @Transactional
    public void join(SignUpDto signUpDto) {
        String name = signUpDto.getName();
        String password = passwordEncoder.encode(signUpDto.getPassword());
        String email = signUpDto.getEmail();
        String phone = signUpDto.getPhone();
        String city = signUpDto.getAddress().getCity();
        String street = signUpDto.getAddress().getStreet();
        String zipcode = signUpDto.getAddress().getZipcode();

        UserRoleEnum role = UserRoleEnum.USER;

        checkUser("이름", userRepository.existsByName(name));
        checkUser("이메일", userRepository.existsByEmail(email));
        checkUser("휴대폰 번호", userRepository.existsByPhone(phone));

        User user = new User(name, password, email, phone, role,
                new Address(city, street, zipcode), false, "chapter");

        userRepository.save(user);
    }

    public ProfileDto getProfile(User user) {
            return new ProfileDto(user);
    }

    /**
     * 유저 중복 확인
     * @param information
     * @param exists
     */
    private void checkUser(String information, boolean exists) {
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 " + information + "입니다.");
        }
    }
}
