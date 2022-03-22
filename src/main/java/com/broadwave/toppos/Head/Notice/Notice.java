package com.broadwave.toppos.Head.Notice;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-02-07
 * Time :
 * Remark : Toppos 본사 공지사항게시판
 */
@Entity
@Data
@Builder
@EqualsAndHashCode(of = "hnId")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="hb_notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hn_id")
    private Long hnId;

    @Column(name="hn_subject")
    private String hnSubject; // 제목

    @Column(length = 100000, name="hn_content")
    private String hnContent; // 내용

    @Column(name="hn_yyyymmdd")
    private String hnYyyymmdd;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
