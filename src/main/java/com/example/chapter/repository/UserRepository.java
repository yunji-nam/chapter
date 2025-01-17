package com.example.chapter.repository;

import com.example.chapter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailAndDeletedFalse(String email);
    boolean existsByPhoneAndDeletedFalse(String phone);
    Optional<User> findByEmailAndDeletedFalse(String email);

}
