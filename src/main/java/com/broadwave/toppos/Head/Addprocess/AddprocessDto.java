package com.broadwave.toppos.Head.Addprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark : Toppos 상품 수선, 추가요금항목, 상용구항목 관련 테이블 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddprocessDto {

    private String baName; // 명칭
    private String baRemark; // 비고

    public String getBaName() {
        return baName;
    }

    public String getBaRemark() {
        return baRemark;
    }
}
