package com.broadwave.toppos.Head.Notice.NoticeFile;

import com.broadwave.toppos.Head.Notice.Notice;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-02-07
 * Time :
 * Remark : Toppos 공지사항 게시판 파일엔티티
 */
@Entity
@Data
@EqualsAndHashCode(of = "hfId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="hb_notice_file")
public class NoticeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hf_id")
    private Long hfId;

    @ManyToOne(targetEntity = Notice.class,fetch = FetchType.LAZY)
    @JoinColumn(name="hn_id")
    private Notice hnId; // 택분실게시판 고유값 ID

    @Column(name="hf_path")
    private String hfPath; // S3파일경로

    @Column(name="hf_filename")
    private String hfFilename; // S3파일명

    @Column(name="hf_original_filename")
    private String hfOriginalFilename; // 원래 파일 명

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
