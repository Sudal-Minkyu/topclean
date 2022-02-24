package com.broadwave.toppos.Manager.TagNotice.Comment;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-02-04
 * Time :
 * Remark : Toppos 지사 택분실게시판 댓글 엔티티
 */
@Entity
@Data
@Builder
@EqualsAndHashCode(of = "hcId")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="hb_tag_notice_comment")
public class TagNoticeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hc_id")
    private Long hcId;

    @Column(name="ht_id")
    private Long htId;

    @Column(name="hc_pre_id")
    private Long hcPreId; // 댓글의 댓글일경우 원댓글의 hcId

    @Column(name="hc_type")
    private String hcType; // 댓글타입 1 : 본문댓글, 2 : 댓글의댓글

    @Column(name="hc_comment")
    private String hcComment;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
