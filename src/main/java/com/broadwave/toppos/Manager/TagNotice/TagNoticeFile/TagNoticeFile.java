package com.broadwave.toppos.Manager.TagNotice.TagNoticeFile;

import com.broadwave.toppos.Manager.TagNotice.TagNotice;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-02-07
 * Time :
 * Remark : Toppos 택분실 게시판 파일엔티티
 */
@Entity
@Data
@EqualsAndHashCode(of = "hfId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="hb_tag_notice_file")
public class TagNoticeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hf_id")
    private Long hfId;

    @ManyToOne(targetEntity = TagNotice.class,fetch = FetchType.EAGER)
    @JoinColumn(name="ht_id")
    private TagNotice htId; // 택분실게시판 고유값 ID

    @Column(name="hf_path")
    private String hfPath; // S3파일경로

    @Column(name="hf_filename")
    private String hfFilename; // S3파일명

    @Column(name="hf_original_filename")
    private String hfOriginalFilename; // 원래 파일 명

    @Column(name="hf_volume")
    private Long hfVolume; // 파일용량

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
