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
 * Remark : Toppos 본사 강제입고현황 왼쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestForceIncomeListDto {

    private BigInteger branchId; // 지사 고정값 ID
    private String brName; // 지사명
    private BigInteger franchiseId; // 가맹점 고정값 ID
    private String frName; // 가맹점명

    private String fdS8Dt; // 가맹점강제입고일
    private BigInteger receiptCount; // 건수
    private BigDecimal fdTotAmt; // 접수총액

    public StringBuffer getFdS8Dt() {
        if(fdS8Dt != null && !fdS8Dt.equals("")){
            StringBuffer getFdS8Dt = new StringBuffer(fdS8Dt);
            getFdS8Dt.insert(4,'-');
            getFdS8Dt.insert(7,'-');
            return getFdS8Dt;
        }else{
            return null;
        }
    }

}
