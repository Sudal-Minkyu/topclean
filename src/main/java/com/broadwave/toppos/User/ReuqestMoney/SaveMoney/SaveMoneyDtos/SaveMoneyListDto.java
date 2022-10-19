package com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-07
 * Time :
 * Remark : Toppos 고객 적립금내역 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveMoneyListDto {

    private Long bcId; // 고객아이디
    private String fsType; // 적립유형
    private Integer fsAmt; // 적립금액 or 사용금액

}
