package com.broadwave.toppos.User.GroupSort.GroupSortDtos;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점별 대분류 정렬 UpdateDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupSortUpdateDto {

    private String bgItemGroupcode; // 대분류 코드
    private String insert_id;
    private LocalDateTime insertDateTime;

}
