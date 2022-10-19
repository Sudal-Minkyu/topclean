package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Minkyu
 * Date : 2022-02-17
 * Time :
 * Remark : Toppos 지사 지사출고현황, 지사강제출고현황 왼쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailBranchReleaseCurrentListDto {

    private String frCode; // 가맹점코드
    private String frName; // 가맹점명
    private String fdSDt; // 출고일자 or 강제출고일자

    private BigDecimal output_cnt; // 출고건수

    private BigDecimal tot_amt; // 접수총액 - 출고건수 금액 sum

}
