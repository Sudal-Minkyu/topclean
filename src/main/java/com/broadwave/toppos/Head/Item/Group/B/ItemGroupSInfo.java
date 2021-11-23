package com.broadwave.toppos.Head.Item.Group.B;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-23
 * Time :
 * Remark : Toppos 상품그룹관리 중분류 테이블
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemGroupSInfo {

    private String bsItemGroupcodeS; // 중분류코드
    private String bgItemGroupcode; // 대분류코드
    private String insert_id;
    private LocalDateTime insertDateTime;

    public String getBsItemGroupcodeS() {
        return bsItemGroupcodeS;
    }

    public String getBgItemGroupcode() {
        return bgItemGroupcode;
    }

    public String getInsert_id() {
        return insert_id;
    }

    public LocalDateTime getInsertDateTime() {
        return insertDateTime;
    }
}
