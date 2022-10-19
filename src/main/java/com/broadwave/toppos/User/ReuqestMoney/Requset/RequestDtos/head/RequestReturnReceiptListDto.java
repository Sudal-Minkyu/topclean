package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.head;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Minkyu
 * Date : 2022-06-07
 * Time :
 * Remark : Toppos 본사 반품현황 왼쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestReturnReceiptListDto {

    private BigInteger branchId; // 지사 고정값 ID
    private String brName; // 지사명
    private BigInteger franchiseId; // 가맹점 고정값 ID
    private String frName; // 가맹점명

    private String fdS6Dt; // 인도일자
    private BigInteger receiptCount; // 건수
    private BigDecimal fdTotAmt; // 접수총액

    public StringBuffer getFdS6Dt() {
        if(fdS6Dt != null && !fdS6Dt.equals("")){
            StringBuffer getFdS6Dt = new StringBuffer(fdS6Dt);
            getFdS6Dt.insert(4,'-');
            getFdS6Dt.insert(7,'-');
            return getFdS6Dt;
        }else{
            return null;
        }
    }

}
