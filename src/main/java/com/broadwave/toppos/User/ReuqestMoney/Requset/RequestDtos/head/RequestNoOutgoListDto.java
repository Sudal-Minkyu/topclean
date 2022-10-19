package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.head;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Minkyu
 * Date : 2022-05-31
 * Time :
 * Remark : Toppos 본사 미출고현황 왼쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestNoOutgoListDto {

    private BigInteger branchId; // 지사 고정값 ID
    private String brName; // 지사명
    private BigInteger franchiseId; // 가맹점 고정값 ID
    private String frName; // 가맹점명

    private String fdEstimateDt; // 출고예정일
    private BigInteger receiptCount; // 건수
    private BigDecimal fdTotAmt; // 접수총액

    public StringBuffer getFdEstimateDt() {
        if(fdEstimateDt != null && !fdEstimateDt.equals("")){
            StringBuffer getFdEstimateDt = new StringBuffer(fdEstimateDt);
            getFdEstimateDt.insert(4,'-');
            getFdEstimateDt.insert(7,'-');
            return getFdEstimateDt;
        }else{
            return null;
        }
    }

}
