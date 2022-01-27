package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-01-25
 * Time :
 * Remark : Toppos 가맹점 일일영업일보 - 총 건수, 총 접수금액 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestBusinessdayListDto {

    private String yyyymmdd; // 접수일자 -> group by --- 완료

    private Integer frQtyAll; // 총건수 - 해당 접수일자의 모든 접수건수 -> sum() --- 완료
    private Integer frTotalAmountAll; // 총 접수금액 --- 완료
}
