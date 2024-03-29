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
 * Remark : Toppos 지사 미출고현황 왼쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailBranchUnReleaseCurrentListDto {

    private String frCode; // 가맹점코드
    private String frName; // 가맹점명
    private Long unoutput_cnt; // 미출고건수
    private Integer tot_amt; // 접수총액

}
