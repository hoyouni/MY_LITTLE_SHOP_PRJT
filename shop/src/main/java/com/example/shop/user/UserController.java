package com.example.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    // 비밀번호 암호화 DI
    private final PasswordEncoder passwordEncoder;

    // 회원 엔티티
    private final UserRepository userRepository;

    // 회원 서비스 클래스 DI
    private final UserService userService;

    /**
     * 회원 로그인 / 회원가입 사이트로 이동
     * @return
     */
    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    /**
     * 회원가입
     * @param userId    회원 아이디
     * @param password  회원 비밀번호
     * @param userNm    회원명
     * @return
     */
    @PostMapping("/regUser")
    public String regUser(String userId, String password, String userNm) throws Exception {
        userService.regUser(userId, password, userNm);
        return "redirect:/product/productList";
    }

}
