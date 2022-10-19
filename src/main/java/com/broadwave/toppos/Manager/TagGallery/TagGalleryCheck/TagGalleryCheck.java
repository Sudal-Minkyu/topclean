package com.broadwave.toppos.Manager.TagGallery.TagGalleryCheck;

import com.broadwave.toppos.Manager.TagGallery.TagGallery;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-03-11
 * Time :
 * Remark : Toppos 지사 택분실 가맹점 체크기록
 */
@Entity
@Data
@Builder
@EqualsAndHashCode(of = "bcId")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="br_tag_gallery_check")
public class TagGalleryCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bc_id")
    private Long bcId;

    @ManyToOne(targetEntity = TagGallery.class,fetch = FetchType.EAGER)
    @JoinColumn(name="bt_id")
    private TagGallery btId; // 택분실 고유값 ID

    @Column(name="fr_code")
    private String frCode; // 가맹점 3자리

    @Column(name="br_tag")
    private String brTag; // 분실택번호 7자리

    @Column(name="br_complete_yn")
    private String brCompleteYn; // 기본값 N, 해당 분실 세탁물을 고객에게 인수완료했을 경우 Y로 변경

    @Column(name="br_complete_dt")
    private LocalDateTime brCompleteDt; // br_complete_yn 이 Y로 변경될떄 시간저장

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
