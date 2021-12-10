package com.broadwave.toppos.Head.Item.Group.A;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-09
 * Time :
 * Remark : Toppos 상품그룹관리 가맹점 전용 GroutSortDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserItemGroupSortDto {

    private Integer bgSort; // 순번
    private String bgItemGroupcode; // 대문류코드
    private String bgName; // 대문류명칭
    private String bgIconFilename; // 아이콘이미지파일명

    public String getBgItemGroupcode() {
        return bgItemGroupcode;
    }

    public Integer getBgSort() {
        if(bgSort==null){
            return 999;
        }else{
            return bgSort;
        }
    }

    public String getBgName() {
        return bgName;
    }

    public String getBgIconFilename() {
        return bgIconFilename;
    }
}
