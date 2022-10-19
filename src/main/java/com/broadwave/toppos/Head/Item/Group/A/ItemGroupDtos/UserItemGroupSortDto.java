package com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-09
 * Time :
 * Remark : Toppos 상품그룹관리 가맹점 전용 GroutSortDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserItemGroupSortDto {

    private Integer bgSort; // 순번
    private String bgFavoriteYn; // 즐겨찾기 YN , 기본값 N
    private String bgItemGroupcode; // 대문류코드
    private String bgName; // 대문류명칭
    private String bgIconFilename; // 아이콘이미지파일명

    public Integer getBgSort() {
        if(bgSort==null){
            return 999;
        }else{
            return bgSort;
        }
    }

}
