package com.broadwave.toppos.User.ReuqestMoney.Requset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-21
 * Time :
 * Remark : Toppos 가맹점 미수금액 리스트 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUnCollectDto {

    private Long bcId; // 고객아이디
    private Integer frTotalAmount; // 합계금액
    private Integer frPayAmount; // 결제금액

    public Long getBcId() {
        return bcId;
    }

    public Integer getFrTotalAmount() {
        return frTotalAmount;
    }

    public Integer getFrPayAmount() {
        return frPayAmount;
    }

    public Integer getUnCollect() {
        return frTotalAmount-frPayAmount; // 미수금액
    }

}
