package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-01-27
 * Time :
 * Remark : Toppos 가맹점 일일영업일보 - 총 접수 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestBusinessdayCustomerListDto {
    private String yyyymmdd;
    private Long totalReceipt; // 접수 건수 -> bcId -> distinct(), sum()
}
