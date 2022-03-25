package com.broadwave.toppos.User.Addprocess.AddprocessDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark : Toppos 상품 수선, 추가요금항목, 상용구항목 관련 테이블 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddprocessDto {

    private Integer baSort; // 정렬순서
    private String baName; // 명칭
    private String baRemark; // 비고

}
