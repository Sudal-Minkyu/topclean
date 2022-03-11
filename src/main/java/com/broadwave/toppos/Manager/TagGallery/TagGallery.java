package com.broadwave.toppos.Manager.TagGallery;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-03-10
 * Time :
 * Remark : Toppos 지사 택분실 갤러리
 */
@Entity
@Data
@Builder
@EqualsAndHashCode(of = "btId")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="br_tag_gallery")
public class TagGallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bt_id")
    private Long btId;

    @Column(name="br_code")
    private String brCode; // 지사코드 2자리

    @Column(name="bt_input_dt")
    private String btInputDt; // 등록일자

    @Column(name="bt_brand_name")
    private String btBrandName; // 추정브랜드명

    @Column(name="bt_input_date")
    private String btInputDate; // 예상 지사입고일

    @Column(name="bt_material")
    private String btMaterial; // 소재

    @Column(name="bt_remark")
    private String btRemark; // 특이사항

    @Column(name="br_close_yn")
    private String brCloseYn; // 종료(게시물내림) 여부 , 기본값 N

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
