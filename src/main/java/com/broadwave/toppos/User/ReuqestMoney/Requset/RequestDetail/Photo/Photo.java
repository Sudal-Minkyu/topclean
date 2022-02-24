package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.Inspeot;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-22
 * Time :
 * Remark : Toppos 사진촬영테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_photo_file")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ff_id")
    private Long id;

    @Column(name="ff_type")
    private String ffType; // 01:접수, 02:가맹점검품, 03:지사검품

    @ManyToOne(targetEntity = RequestDetail.class,fetch = FetchType.LAZY)
    @JoinColumn(name="fd_id")
    private RequestDetail fdId; // 접수 세부테이블 ID

    @ManyToOne(targetEntity = Inspeot.class,fetch = FetchType.LAZY)
    @JoinColumn(name="fi_id")
    private Inspeot fiId; // 검품세부테이블 ID

    @Column(name="ff_path")
    private String ffPath; // S3파일경로

    @Column(name="ff_filename")
    private String ffFilename; // S3파일명

    @Column(name="ff_remark")
    private String ffRemark; // 특이사항

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
