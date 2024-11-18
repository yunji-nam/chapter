package com.example.chapter.service;

import com.example.chapter.dto.ProfileDto;
import com.example.chapter.dto.SignUpDto;
import com.example.chapter.dto.UpdateProfileDto;
import com.example.chapter.entity.Address;
import com.example.chapter.entity.User;
import com.example.chapter.entity.UserRoleEnum;
import com.example.chapter.exception.DuplicateFieldException;
import com.example.chapter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입
    @Transactional
    public void join(SignUpDto signUpDto) {

        String name = signUpDto.getName();
        log.info("name:" + name);
        String password = passwordEncoder.encode(signUpDto.getPassword());
        log.info("password:" + password);
        String email = signUpDto.getEmail();
        String phone = signUpDto.getPhone();

        String zipcode = signUpDto.getAddress().getZipcode();
        String street = signUpDto.getAddress().getStreet();
        String detail = signUpDto.getAddress().getDetail();


        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(name, password, validateEmail(email), validatePhone(phone), role,
                new Address(zipcode, street, detail), "chapter");

        userRepository.save(user);
    }

    // 프로필 조회
    public ProfileDto getProfile(User user) {
        return new ProfileDto(user);
    }

    // 프로필 수정
    @Transactional
    public void updateProfile(User user, UpdateProfileDto dto) {

        String password = dto.getPassword() != null ? passwordEncoder.encode(dto.getPassword()) : user.getPassword();
        String email = dto.getEmail() != null ? validateEmail(dto.getEmail()) : user.getEmail();
        String phone = dto.getPhone() != null ? validatePhone(dto.getPhone()) : user.getPhone();

        Address address = updateAddress(user.getAddress(), dto.getAddress());

        user.updateUser(password, email, phone, address);

    }

    private String validateEmail(String email) {
        if (userRepository.existsByEmail(email)) throw new DuplicateFieldException("이메일", email);
        return email;
    }

    private String validatePhone(String phone) {
        if (userRepository.existsByPhone(phone)) throw new DuplicateFieldException("휴대폰 번호", phone);
        return phone;
    }

    private Address updateAddress(Address currentAddress, Address newAddress) {
        if (newAddress == null) {
            return currentAddress;
        }
        return new Address(
                newAddress.getZipcode() != null ? newAddress.getZipcode() : currentAddress.getZipcode(),
                newAddress.getStreet() != null ? newAddress.getStreet() : currentAddress.getStreet(),
                newAddress.getDetail() != null ? newAddress.getDetail() : currentAddress.getDetail()
        );
    }

}
