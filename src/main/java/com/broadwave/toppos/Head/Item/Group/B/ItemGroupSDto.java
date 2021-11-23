package com.broadwave.toppos.Head.Item.Group.B;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark : Toppos 상품그룹관리 중분류 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemGroupSDto {

    private String bsItemGroupcodeS; // 중분류코드
    private String bgItemGroupcode; // 대분류코드
    private String bgName; // 대분류명칭
    private String bsName; // 중분류명칭
    private String bsRemark; // 특이사항

    public String getBgName() {
        return bgName;
    }

    public String getBsItemGroupcodeS() {
        return bsItemGroupcodeS;
    }

    public String getBgItemGroupcode() {
        return bgItemGroupcode;
    }

    public String getBsName() {
        return bsName;
    }

    public String getBsRemark() {
        return bsRemark;
    }
}
