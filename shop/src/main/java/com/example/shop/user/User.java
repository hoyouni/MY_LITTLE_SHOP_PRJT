package com.example.shop.user;

import com.example.shop.sale.Sales;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원 엔티티
 */
@Entity
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;                 // real id , 사용자 별 구분자로 사용

    @Column(unique = true)
    public String userId;           // 회원 아이디
    public String userNm;           // 회원명
    public String password;         // 비밀번호
    public String userGrade;        // 회원 등급

    /**
     * 일대다 관계
     * 하나의 유저는 여러개의 주문이 가능하다. (User 가 드라이빙 테이블)
     * mappedBy 뒤에는 내 컬럼을 훔쳐쓰고있는 다른 컬럼명 기입하면 됨
     * 이렇게 하면 유저정보 조회 시 해당 유저가 주문한 정보도 같이 출력됨
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    List<Sales> sales = new ArrayList<>();
}
