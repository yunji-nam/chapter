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
                .deleted(false)
                .provider("chapter").build();

        userRepository.save(user);
    }

    // 프로필 조회
    public ProfileDto getProfile(User user) {
        User findUser = getUser(user.getId());
        return new ProfileDto(findUser);
    }

    // 프로필 수정
    @Transactional
    public void updateProfile(User user, UpdateProfileDto dto) {
        User findUser = getUser(user.getId());
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
        } else {
            if (dto.getAddress() != null) {
                String zipcode = dto.getAddress().getZipcode();
                String street = dto.getAddress().getStreet();
                String detail = dto.getAddress().getDetail();
                address = new Address(zipcode, street, detail);
            }
        }

        findUser.updateProfile(email, phone, address);
    }

    // 비밀번호 수정
    @Transactional
    public void updatePassword(User user, PasswordDto dto) {
        User findUser = getUser(user.getId());
        if (!passwordEncoder.matches(dto.getCurrentPassword(), findUser.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 틀립니다.");
        }
        String newPassword = passwordEncoder.encode(dto.getNewPassword());
        findUser.updatePassword(newPassword);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user를 찾을 수 없습니다."));
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

    @Transactional
    public void withdraw(Long userId, User user) {
        if (!userId.equals(user.getId()) && !user.isAdmin()) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        User findUser = getUser(userId);
        findUser.delete();
    }
}
