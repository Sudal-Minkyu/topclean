package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Minkyu
 * Date : 2022-02-18
 * Time :
 * Remark : Toppos 지사 가맹점반송현황 왼쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailBranchReturnCurrentListDto {

    private String frCode; // 가맹점코드
    private String frName; // 가맹점명
    private String fdS3Dt; // 반송일자
    private BigDecimal return_cnt; //반송건수
    private BigDecimal tot_amt; // 접수총액

}
