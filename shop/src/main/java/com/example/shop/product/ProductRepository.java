package com.example.shop.product;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DB 입출력하기 위한 Product 테이블 (엔티티) 의 인터페이스
 * JpaRepository 를 상속받아야 함
 * 상속 받을때 어떤 엔티티를 가져올지와 해당 엔티티의 id 타입을 작성 해줘야함
 * 인터페이스 생성 시 클래스도 자동으로 생성됨
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

}
