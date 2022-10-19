package com.broadwave.toppos.Manager.TagNotice;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-01-28
 * Time :
 * Remark : Toppos 지사 택분실게시판
 */
@Entity
@Data
@Builder
@EqualsAndHashCode(of = "htId")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="hb_tag_notice")
public class TagNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ht_id")
    private Long htId;

    @Column(name="br_code")
    private String brCode; // 지사코드 2자리

    @Column(name="ht_subject")
    private String htSubject; // 제목

    @Column(length = 100000, name="ht_content")
    private String htContent; // 내용

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
