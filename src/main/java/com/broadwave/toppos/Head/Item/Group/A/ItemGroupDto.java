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
 * Remark : Toppos 상품그룹관리 대분류 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemGroupDto {

    private String bgItemGroupcode; // 대분류코드

    private String bgName; // 대문류명칭

    private String bgRemark; // 특이사항

    private String bgUseYn; // 사용여부

    public String getBgUseYn() {
        return bgUseYn;
    }

    public String getBgItemGroupcode() {
        return bgItemGroupcode;
    }

    public String getBgName() {
        return bgName;
    }

    public String getBgRemark() {
        return bgRemark;
    }

    @Override
    public String toString() {
        return "ItemGroup [bgItemGroupcode=" + bgItemGroupcode + ", bgName=" + bgName + ", bgRemark=" + bgRemark+"]";
    }

}
