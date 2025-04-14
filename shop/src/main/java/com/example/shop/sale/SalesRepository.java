package com.example.shop.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 주문 엔티티에 대한 인터페이스
 */
public interface SalesRepository extends JpaRepository<Sales, Long> {
    // JPQL 문법을 활용한 SQL 처리 > 참조되는(User) 의 모든 컬럼 정보를 출력함
    @Query(value = "SELECT s FROM Sales s JOIN FETCH s.user")
    List<Sales> customFindAll();
}

