package com.example.shop.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 상품 댓글 엔티티
 */
@Entity
@Getter
@Setter
@ToString
public class ProductComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;         // 댓굴 번호

    private String userId;          // 작성자

    @Column(length = 1000)
    private String content;         // 댓글 내용

    private Long parentId;          // 상품번호

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime indt;     // 등록일자

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updt;     // 수정일자
}
