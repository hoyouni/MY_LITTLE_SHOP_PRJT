package com.example.shop.notice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Notice 공지사항 엔티티 (테이블)
 */
@Entity
@ToString
@Getter
@Setter
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notice_id;          // 공지사항 번호

    private String notice_title;     // 공지사항 제목

    @Temporal(TemporalType.TIMESTAMP)
    private Date indt;               // 등록일자

    @Temporal(TemporalType.TIMESTAMP)
    private Date updt;               // 수정일자

}
