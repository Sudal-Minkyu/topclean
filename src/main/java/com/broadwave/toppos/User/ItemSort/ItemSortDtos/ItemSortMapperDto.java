package com.broadwave.toppos.User.ItemSort.ItemSortDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점별 상품 정렬 MapperDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSortMapperDto {

    private String biItemcode; // 대분류 코드
    private Integer bfSort; // 정렬순서

}
