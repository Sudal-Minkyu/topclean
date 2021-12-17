package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-17
 * Time :
 * Remark : Toppos 가맹점 접수세부 가격관련 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailAmtDto {
    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )

    public Integer getFdTotAmt() {
        return fdTotAmt;
    }
}
