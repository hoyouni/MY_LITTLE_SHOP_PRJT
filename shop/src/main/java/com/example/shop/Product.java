package com.example.shop;

import jakarta.persistence.*;

/**
 * Product 클래스이자 엔티티이자 테이블 생성
 *  >> SQL 언어에서는 create table 어쩌고 해서 만들었는데 JPA 는 클래스로 간단하게 생성할 수 있음.
 *     Entity 어노테이션을 사용해서 테이블임을 명시해줘야함
 *     Column 어노테이션을 통해 컬럼 제약설정이나 기타 컬럼 속성 부여 가능
 *     ex) @Column(unique = true) , @Column(nullable = false) 등..
 *     다만 코드를 통해 테이블 속성 변경하는 경우에는 바로 반영이 안 될 수도 있어서
 *     drop table 후에 다시 코드를 빌드 하거나
 *     아니면 처음부터 dbeaver 에서 속성 변경 해주는게 빠를 수도 있음.
 */
@Entity
public class Product {
    /**
     * @Id 어노테이션을 사용하여 각 인스턴스 (row) 별로 구분자를 만들어줘야함, 뭐 pk 와 같은 기본키라고 보면 될 듯.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) 를 사용하게 되면 SQL 에서 자동 시퀀스 마냥 알아서 번호를 1씩 올려서 채번해줌
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long prodCd;    // 상품코드

    public String prodNm;  // 상품명

    // 컬럼용 변수에는 int 말고 Integer 를 사용하도록 권장함
    public Integer price;  // 상품금액
}








