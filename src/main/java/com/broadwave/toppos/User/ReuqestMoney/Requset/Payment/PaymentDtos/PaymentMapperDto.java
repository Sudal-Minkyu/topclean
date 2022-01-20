package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark : Toppos 가맹점 접수 결제테이블 MapperDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMapperDto {
    private Long bcId; // 고객 고유ID값
    private String frNo; // 접수코드

    public Long getBcId() {
        return bcId;
    }

    public String getFrNo() {
        return frNo;
    }
}
