package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-22
 * Time :
 * Remark : Toppos 가맹점 접수마스트 RequestSearchDto 삭제조회용
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestSearchDto {

    private String frNo; // 접수코드

    public String getFrNo() {
        return frNo;
    }
}
