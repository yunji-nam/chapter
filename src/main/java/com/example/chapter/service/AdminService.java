package com.example.chapter.service;

import com.example.chapter.dto.SignUpDto;
import com.example.chapter.dto.UserInfo;
import com.example.chapter.entity.User;
import com.example.chapter.entity.UserRoleEnum;
import com.example.chapter.exception.DuplicateFieldException;
import com.example.chapter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .deleted(false)
                .provider("chapter").build();

        userRepository.save(user);
    }

    public Page<UserInfo> searchUser(String query, String condition, Pageable pageable) {
        Page<User> page = switch (condition) {
            case "name" -> userRepository.findByNameContainingIgnoreCase(query, pageable);
            case "email" -> userRepository.findByEmailContainingIgnoreCase(query, pageable);
            default -> throw new IllegalArgumentException("유효하지 않은 값입니다.");
        };
        return page.map(UserInfo::new);
    }

    private String validateEmail(String email) {
        if (userRepository.existsByEmail(email)) throw new DuplicateFieldException("이메일", email);
        return email;
    }

    private String validatePhone(String phone) {
        if (userRepository.existsByPhone(phone)) throw new DuplicateFieldException("휴대폰 번호", phone);
        return phone;
    }

    public Page<UserInfo> getUsers(Pageable pageable) {
        Pageable getPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> users = userRepository.findAll(getPageable);
        return users.map(UserInfo::new);
    }
}
