package com.example.shop.sale;

import com.example.shop.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 주문 엔티티
 */
@Entity
@Getter
@Setter
@ToString
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;                            // 주문번호
    String prodNm;                      // 상품코드
    Integer price;                      // 가격
    Integer orderCnt;                   // 주문수량
    //Long userRealId;                    // 유저 고유번호 (User 엔티티의 Id)

    /**
     * 다대일 테이블 조인으로 기준이 되는 테이블은 Sales
     * 여러 주문건에 대한 하나의 유저 정보 필요시 해당 어노테이션 사용
     * 1. 대신 N+1 실행으로 성능상 이슈 발생 가능성 있음. > LAZY 작성해주면 필요시에만 실행시켜줌
     * 2. 참조되는 테이블의 모든 컬럼 정보를 가져와버림 (보안상 문제 가능성)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="user_id",     //  작명 자유롭게 기입
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)  // 무결성 제약조건 해제
    )
    private User user;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime indt;         // 등록일자

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updt;         // 수정일자
}
