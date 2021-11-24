package com.broadwave.toppos.Head.Item.Group.C;

import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupS;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark : Toppos 상품그룹관리 소재 상품 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_item")
public class Item {

    @Id
    @Column(name="bi_itemcode")
    private String biItemcode; // 상품코드

    @Column(name="bg_item_groupcode")
    private String bgItemGroupcode; // 대분류코드

    @Column(name = "bs_item_groupcode_s")
    private String bsItemGroupcodeS; // 중분류코드

    @Column(name="bi_item_sequence")
    private String biItemSequence; // 상품순번

    @Column(name="bi_name")
    private String biName; // 상품명

    @Column(name="bi_remark")
    private String biRemark; // 특이사항

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
