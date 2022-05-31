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
 * Remark : Toppos 본사 출고현황 왼쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOutgoListDto {

    private BigInteger branchId; // 지사 고정값 ID
    private String brName; // 지사명
    private BigInteger franchiseId; // 가맹점 고정값 ID
    private String frName; // 가맹점명

    private String fdS4Dt; // 지사출고일
    private BigInteger requestCount; // 건수
    private BigDecimal fdTotAmt; // 접수총액

    public StringBuffer getFdS4Dt() {
        if(fdS4Dt != null && !fdS4Dt.equals("")){
            StringBuffer getFdS4Dt = new StringBuffer(fdS4Dt);
            getFdS4Dt.insert(4,'-');
            getFdS4Dt.insert(7,'-');
            return getFdS4Dt;
        }else{
            return null;
        }
    }

}
