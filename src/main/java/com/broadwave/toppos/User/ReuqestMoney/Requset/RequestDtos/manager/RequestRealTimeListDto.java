package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Minkyu
 * Date : 2022-04-01
 * Time :
 * Remark : Toppos 실시간접수현황 왼쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRealTimeListDto {

    private String frName; // 가맹점명
    private String frYyyymmdd; // 접수일자
    private BigInteger customerCount; // 고객수
    private BigInteger requestCount; // 상품수
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
