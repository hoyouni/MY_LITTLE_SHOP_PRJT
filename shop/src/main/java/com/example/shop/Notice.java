package com.example.shop;

import jakarta.persistence.*;

import java.util.Date;

/**
 * Notice 공지사항 엔티티 (테이블)
 */
@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long notice_id;          // 공지사항 번호

    public String notice_title;     // 공지사항 제목

    @Temporal(TemporalType.TIMESTAMP)
    public Date indt;               // 등록일자

    @Temporal(TemporalType.TIMESTAMP)
    public Date updt;               // 수정일자

}
