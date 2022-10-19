package com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-23
 * Time :
 * Remark : Toppos 상품그룹관리 중분류 테이블
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemGroupSInfo {

    private String bsItemGroupcodeS; // 중분류코드
    private String bgItemGroupcode; // 대분류코드
    private String insert_id;
    private LocalDateTime insertDateTime;

}
