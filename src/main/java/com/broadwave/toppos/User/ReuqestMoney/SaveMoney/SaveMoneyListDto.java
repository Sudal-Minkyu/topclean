package com.broadwave.toppos.User.ReuqestMoney.SaveMoney;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-07
 * Time :
 * Remark : Toppos 고객 적립금내역 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveMoneyListDto {

    private Long bcId; // 고객아이디
    private String fsType; // 적립유형
    private Integer fsAmt; // 적립금액 or 사용금액

    public Long getBcId() {
        return bcId;
    }

    public String getFsType() {
        return fsType;
    }

    public Integer getFsAmt() {
        return fsAmt;
    }
}
