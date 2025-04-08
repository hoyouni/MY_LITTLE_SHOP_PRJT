package com.example.shop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정 클래스
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // filterChain 은 클라이언트 요청과 서버 응답 사이에서 자동으로 실행하고자 하는 코드를 담는 곳
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /**
         * csrf : 타 사이트에서 위조를 하여 본인 사이트 api 호출을 하여 공격할 수 없도록 하는 보안 기능
         * 개발을 위해 잠깐 off
         */
        http.csrf((csrf) -> csrf.disable());
        // 모든 URL 접속시 모든 유저의 접속을 허락하게 끔 permitAll 함수 사용
        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/**").permitAll()
        );
        return http.build();
    }
}
