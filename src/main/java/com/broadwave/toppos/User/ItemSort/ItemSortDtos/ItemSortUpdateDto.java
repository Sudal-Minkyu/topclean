package com.broadwave.toppos.User.ItemSort.ItemSortDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점별 상품 정렬 UpdateDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSortUpdateDto {

    private String biItemcode; // 상품 코드
    private String insert_id;
    private LocalDateTime insertDateTime;

    public String getBiItemcode() {
        return biItemcode;
    }

    public String getInsert_id() {
        return insert_id;
    }

    public LocalDateTime getInsertDateTime() {
        return insertDateTime;
    }

}
