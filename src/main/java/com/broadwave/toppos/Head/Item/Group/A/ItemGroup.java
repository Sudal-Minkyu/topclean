package com.broadwave.toppos.Head.Item.Group.A;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-17
 * Time :
 * Remark : Toppos 상품그룹관리 대분류 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_item_group")
public class ItemGroup {

    @Id
    @Column(name="bg_item_groupcode")
    private String bgItemGroupcode; // 대분류코드

    @Column(name="bg_name")
    private String bgName; // 대문류명칭

    @Column(name="bg_remark")
    private String bgRemark; // 특이사항

    @Column(name="bg_use_yn")
    private String bgUseYn; // 사용여부

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
