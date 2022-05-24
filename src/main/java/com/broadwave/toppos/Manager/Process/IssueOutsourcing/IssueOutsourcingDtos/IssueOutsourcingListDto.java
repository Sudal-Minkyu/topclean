package com.broadwave.toppos.Manager.Process.IssueOutsourcing.IssueOutsourcingDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Minkyu
 * Date : 2022-05-23
 * Time :
 * Remark : Toppos 지사 외주/출고 현황 왼쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueOutsourcingListDto {

    private String frName; // 가맹점명
    private String fdO1Dt; // 지사외주 출고일

    private BigDecimal deliveryCount; // 출고건수
    private BigDecimal receiptCount; // 입고건수

    private BigDecimal fdTotAmt; // 접수총액
    private BigDecimal fdOutsourcingAmt; // 외주총액

    public StringBuffer getFdO1Dt() {
        if(fdO1Dt != null && !fdO1Dt.equals("")){
            StringBuffer getFdO1Dt = new StringBuffer(fdO1Dt);
            getFdO1Dt.insert(4,'-');
            getFdO1Dt.insert(7,'-');
            return getFdO1Dt;
        }else{
            return null;
        }
    }

}
