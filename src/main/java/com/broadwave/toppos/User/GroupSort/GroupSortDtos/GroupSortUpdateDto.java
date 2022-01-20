package com.broadwave.toppos.User.GroupSort.GroupSortDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점별 대분류 정렬 UpdateDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupSortUpdateDto {

    private String bgItemGroupcode; // 대분류 코드
    private String insert_id;
    private LocalDateTime insertDateTime;

    public String getBgItemGroupcode() {
        return bgItemGroupcode;
    }

    public String getInsert_id() {
        return insert_id;
    }

    public LocalDateTime getInsertDateTime() {
        return insertDateTime;
    }

}
