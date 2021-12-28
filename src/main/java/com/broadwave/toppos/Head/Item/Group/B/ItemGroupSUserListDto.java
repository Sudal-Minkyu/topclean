package com.broadwave.toppos.Head.Item.Group.B;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점 상품순서 가져오는 리스트 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemGroupSUserListDto {

    private String bsItemGroupcodeS; // 중분류코드
    private String bgItemGroupcode; // 대분류코드
    private String bgName; // 대분류명칭
    private String bsName; // 중분류명칭

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

}
