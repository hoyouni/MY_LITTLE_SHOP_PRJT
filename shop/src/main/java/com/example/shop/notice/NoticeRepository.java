package com.example.shop.notice;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DB 입출력하기 위한 공지사항 (Notice 테이블 (엔티티)) 의 인터페이스
 */
public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
