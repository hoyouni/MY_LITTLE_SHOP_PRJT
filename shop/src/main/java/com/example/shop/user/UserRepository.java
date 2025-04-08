package com.example.shop.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 회원 아이디로 검색하는 인터페이스
    Optional<User> findByUserId(String userId);
}
