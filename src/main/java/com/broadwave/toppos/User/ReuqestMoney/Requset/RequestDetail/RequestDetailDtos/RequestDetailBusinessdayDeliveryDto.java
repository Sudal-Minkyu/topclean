package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-01-27
 * Time :
 * Remark : Toppos 가맹점 일일영업일보 출고 카운트
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailBusinessdayDeliveryDto {
    private String yyyymmdd;
    private Long totalDelivery; // 총 출고 - RequestDatail의 fdS6Dt가 frYyyymmdd와 같은 수
}
