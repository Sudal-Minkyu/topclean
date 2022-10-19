package com.broadwave.toppos.User.GroupSort.GroupSortDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점별 대분류 정렬 MapperDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupSortMapperDto {

    private String bgItemGroupcode; // 대분류 코드
    private String bgFavoriteYn; // 즐겨찾기 YN , 기본값 N
    private Integer bgSort; // 정렬순서

}
