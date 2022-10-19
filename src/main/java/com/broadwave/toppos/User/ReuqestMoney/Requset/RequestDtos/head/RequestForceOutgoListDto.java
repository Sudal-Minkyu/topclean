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
 * Remark : Toppos 본사 강제출고현황 왼쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestForceOutgoListDto {

    private BigInteger branchId; // 지사 고정값 ID
    private String brName; // 지사명
    private BigInteger franchiseId; // 가맹점 고정값 ID
    private String frName; // 가맹점명

    private String fdS7Dt; // 지사강제출고일
    private BigInteger receiptCount; // 건수
    private BigDecimal fdTotAmt; // 접수총액

    public StringBuffer getFdS7Dt() {
        if(fdS7Dt != null && !fdS7Dt.equals("")){
            StringBuffer getFdS7Dt = new StringBuffer(fdS7Dt);
            getFdS7Dt.insert(4,'-');
            getFdS7Dt.insert(7,'-');
            return getFdS7Dt;
        }else{
            return null;
        }
    }

}
