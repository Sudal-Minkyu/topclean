package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2022-03-04
 * Time :
 * Remark : Toppos 지사 메인페이지 일주일간 접수한 금액 그래프용 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestWeekAmountDto {

    private String frName; // 가맹점명
    private Integer value; // 접수금액 합계

}
