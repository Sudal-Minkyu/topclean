package com.broadwave.toppos.Head.Item.Group.B;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark : Toppos 상품그룹관리 중분류 가맹점접수페이지 전용 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserItemGroupSListDto {

    private String bgItemGroupcode; // 대분류코드
    private String bsItemGroupcodeS; // 중분류코드
    private String bsName; // 중분류명칭

    public String getBsItemGroupcodeS() {
        return bsItemGroupcodeS;
    }

    public String getBgItemGroupcode() {
        return bgItemGroupcode;
    }

    public String getBsName() {
        return bsName;
    }

}
