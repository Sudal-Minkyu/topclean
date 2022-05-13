package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.user;

import com.broadwave.toppos.User.Customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-02-07
 * Time :
 * Remark : Toppos 가맹점 영수증 출력 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPaymentPaperDto {

    private String frNo;

    private String frCode; // 가맹점코드
    private String frName; // 가맹점명
    private String frBusinessNo;
    private String frRpreName;
    private String frTelNo;
    private String frTagType;

    private Customer customer;

    private String frYyyymmdd;
    private Integer frNormalAmount;
    private Integer frDiscountAmount;
    private Integer frTotalAmount;
    private Integer frPayAmount;

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
