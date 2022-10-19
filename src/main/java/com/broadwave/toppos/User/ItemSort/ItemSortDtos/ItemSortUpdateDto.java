package com.broadwave.toppos.User.ItemSort.ItemSortDtos;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점별 상품 정렬 UpdateDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSortUpdateDto {

    private String biItemcode; // 상품 코드
    private String insert_id;
    private LocalDateTime insertDateTime;

}
