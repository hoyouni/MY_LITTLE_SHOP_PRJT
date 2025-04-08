package com.example.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 비즈니스 로직 처리를 위한 회원 Service 클래스 생성
 */
@Service
@RequiredArgsConstructor
public class UserService {

    // 비밀번호 암호화 DI
    private final PasswordEncoder passwordEncoder;

    // 회원 엔티티
    private final UserRepository userRepository;

    // 회원 등록
    public void regUser(String userId, String password, String userNm) throws Exception {

        if (userId.length() < 4) {
            throw new Exception("아이디는 4자리 이상 입력해야 합니다.");
        }
        else if(password.length() < 8) {
            throw new Exception("비밀번호는 8자리 이상 입력해야 합니다.");
        }

        User user = new User();
        user.setUserId(userId);
        // 비밀번호 암호화해서 저장
        var hash = passwordEncoder.encode(password);
        user.setPassword(hash);
        user.setUserNm(userNm);
        user.setUserGrade("user");      // 관리자 제외 모든 등급은 user 세팅

        userRepository.save(user);
    }
}
