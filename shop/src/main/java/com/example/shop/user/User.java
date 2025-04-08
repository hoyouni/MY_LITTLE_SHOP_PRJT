package com.example.shop.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 엔티티
 */
@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;                 // real id , 사용자 별 구분자로 사용

    @Column(unique = true)
    public String userId;           // 회원 아이디
    public String userNm;           // 회원명
    public String password;         // 비밀번호
    public String userGrade;        // 회원 등급
}
