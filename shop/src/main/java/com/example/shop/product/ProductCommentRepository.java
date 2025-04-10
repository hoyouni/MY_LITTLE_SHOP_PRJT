package com.example.shop.product;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품 댓글 엔티티 관련 인터페이스
 */
public interface ProductCommentRepository extends JpaRepository<ProductComment, Long> {

}