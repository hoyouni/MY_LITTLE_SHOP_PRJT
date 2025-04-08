package com.example.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 비즈니스 로직 처리를 위한 회원 Service 클래스 생성
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

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

    /**
     * 회원 로그인 기능
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // DB 에서 회원 아이디 검색 하여 조회
        Optional<User> user = userRepository.findByUserId(username);

        // user 가 null 인 경우
        if (user.isEmpty()){
            throw new UsernameNotFoundException("아이디를 확인해주시기 바랍니다.");
        }
        // user 정보 있을 시
        else if(user.isPresent()) {
            // 권한 처리용 list
            List<GrantedAuthority> userGradeList = new ArrayList<>();

            // user 에서 정보 가져와서 User 엔티티에 담아줌
            User userInfo = user.get();

            // 유저 정보 중 권한 컬럼이 admin 으로 저장되어 있을 경우 권한 처리
            if(userInfo.getUserGrade().equals(("admin"))) {
                userGradeList.add(new SimpleGrantedAuthority("admin"));
            }
            // 그 외에는 전부 user 처리
            else {
                userGradeList.add(new SimpleGrantedAuthority("user"));
            }
            /**
             * 유저 정보 리턴
             * 이 때 권한 같은 경우에는 회원가입 시 DB 에서 권한을 저장해두는데
             * 크로스 체크 겸 DB 에 저장된 회원의 등급별로 권한 리스트에도 같이 저장해둠
             */
            return new org.springframework.security.core.userdetails.User(userInfo.getUserId(), userInfo.getPassword(), userGradeList);
        }
        return null;
    }
}
