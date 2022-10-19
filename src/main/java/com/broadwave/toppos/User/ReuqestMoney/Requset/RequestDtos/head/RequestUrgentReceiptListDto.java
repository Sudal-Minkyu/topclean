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
 * Remark : Toppos 본사 세탁현황(일반,특급,급세탁, 당일세탁) 왼쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUrgentReceiptListDto {

    private BigInteger branchId; // 지사 고정값 ID
    private String brName; // 지사명
    private BigInteger franchiseId; // 가맹점 고정값 ID
    private String frName; // 가맹점명

    private String frYyyymmdd; // 접수일자
    private BigInteger receiptCount; // 건수
    private BigDecimal fdTotAmt; // 접수총액

    public StringBuffer getFrYyyymmdd() {
        if(frYyyymmdd != null && !frYyyymmdd.equals("")){
            StringBuffer getFrYyyymmdd = new StringBuffer(frYyyymmdd);
            getFrYyyymmdd.insert(4,'-');
            getFrYyyymmdd.insert(7,'-');
            return getFrYyyymmdd;
        }else{
            return null;
        }
    }

}
