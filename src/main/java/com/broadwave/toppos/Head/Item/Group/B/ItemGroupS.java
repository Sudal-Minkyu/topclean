package com.broadwave.toppos.Head.Item.Group.B;

import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.ItemGroupPK;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark : Toppos 상품그룹관리 중분류 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(ItemGroupPK.class)
@Table(name="bs_item_group_s")
public class ItemGroupS {

    @Id
    @Column(name="bs_item_groupcode_s")
    private String bsItemGroupcodeS; // 중분류코드

    @Id
    @ManyToOne(targetEntity = ItemGroup.class,fetch = FetchType.LAZY)
    @JoinColumn(name="bg_item_groupcode")
    private ItemGroup bgItemGroupcode; // 대분류코드

    @Column(name="bs_name")
    private String bsName; // 중분류명칭

    @Column(name="bs_remark")
    private String bsRemark; // 특이사항

    @Column(name="bs_use_yn")
    private String bsUseYn; // 사용여부

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
