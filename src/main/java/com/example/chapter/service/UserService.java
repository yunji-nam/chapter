package com.example.chapter.service;

import com.example.chapter.dto.PasswordDto;
import com.example.chapter.dto.ProfileDto;
import com.example.chapter.dto.SignUpDto;
import com.example.chapter.dto.UpdateProfileDto;
import com.example.chapter.entity.Address;
import com.example.chapter.entity.User;
import com.example.chapter.entity.UserRoleEnum;
import com.example.chapter.exception.DuplicateFieldException;
import com.example.chapter.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

        if (validateEmail(email)) {
            throw new DuplicateFieldException("이메일", email);
        }

        if (validatePhone(phone)) {
            throw new DuplicateFieldException("휴대폰 번호", phone);
        }

        String zipcode = signUpDto.getAddress().getZipcode();
        String street = signUpDto.getAddress().getStreet();
        String detail = signUpDto.getAddress().getDetail();


        UserRoleEnum role = UserRoleEnum.USER;

        User user = User.builder()
                .name(name)
                .password(password)
                .email(email)
                .phone(phone)
                .role(role)
                .address(new Address(zipcode, street, detail))
                .isDelete(false)
                .provider("chapter").build();

        userRepository.save(user);
    }

    // 프로필 조회
    public ProfileDto getProfile(User user) {
        User findUser = getUser(user);
        return new ProfileDto(findUser);
    }

    // 프로필 수정
    @Transactional
    public void updateProfile(User user, UpdateProfileDto dto) {
        User findUser = getUser(user);
        String email = findUser.getEmail();
        if (!dto.getEmail().equals(email)) {
            if (validateEmail(dto.getEmail())) {
                throw new DuplicateFieldException("이메일", dto.getEmail());
            }
            email = dto.getEmail();
        }

        String phone = findUser.getPhone();
        if ((phone == null && dto.getPhone() != null) || (phone != null && !dto.getPhone().equals(phone))) {
            if (validatePhone(dto.getPhone())) {
                throw new DuplicateFieldException("휴대폰 번호", dto.getPhone());
            }
            phone = dto.getPhone();
        }

        Address address = findUser.getAddress();
        if (address != null) {
            address = updateAddress(findUser.getAddress(), dto.getAddress());
        }

        findUser.update(email, phone, address);

        log.info("user email:" + user.getEmail());
        log.info("user phone:" + user.getPhone());
    }

    private User getUser(User user) {
        return userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("user를 찾을 수 없습니다."));
    }

    // 비밀번호 확인
    public boolean checkPassword(User user, PasswordDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        return user.getPassword().equals(encodedPassword);
    }

    private boolean validateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean validatePhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    private Address updateAddress(Address currentAddress, Address newAddress) {
        String zipcode = currentAddress.getZipcode();
        String street = currentAddress.getStreet();
        String detail = currentAddress.getDetail();
        if (zipcode != null && !newAddress.getZipcode().equals(currentAddress.getZipcode())) {
            zipcode = newAddress.getZipcode();
        }
        if (street != null && !newAddress.getStreet().equals(currentAddress.getStreet())) {
            street = newAddress.getStreet();
        }
        if (detail != null && !newAddress.getDetail().equals(currentAddress.getDetail())) {
            detail = newAddress.getDetail();
        }
        return new Address(zipcode, street, detail);
    }

}
