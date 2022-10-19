package com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-01-26
 * Time :
 * Remark : Toppos 가맹점 일일영업일보 재세탁, 부착물, 총출고 카운트
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveMoneyBusinessdayListDto {
    private String yyyymmdd;
    private Integer fsAmtType02All; // SaveMoney 적림금사용 fsType = "02"인 fsAmt -> sum()
}
