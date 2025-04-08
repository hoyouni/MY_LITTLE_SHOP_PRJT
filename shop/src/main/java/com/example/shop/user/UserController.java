package com.example.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 회원 정보 관련 클래스
 */
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
     * 회원가입 사이트 호출
     * @return
     */
    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    /**
     * 회원가입 기능
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

    /**
     * 로그인 페이지 호출
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    /**
     * 마이 페이지 호출
     * @return
     */
    @GetMapping("/myPage")
    public String myPage(Authentication auth) {
        // System.out.println(auth);                    // 각종 권한 정보 출력
        // System.out.println(auth.getName());          // 아이디출력가능
        // System.out.println(auth.isAuthenticated());  // 로그인여부 검사가능
        // System.out.println(auth.getAuthorities().contains(new SimpleGrantedAuthority("admin"))); // 현재 유저 권한
        return "myPage.html";
    }

}
