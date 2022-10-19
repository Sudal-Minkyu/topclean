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

    private String frOpenWeekday; // 평일 오픈시간
    private String frOpenSaturday; // 토요일 오픈시간
    private String frOpenHoliday; // 휴일 오픈시간
    private String frCloseWeekday; // 평일 마감시간
    private String frCloseSaturday; // 토요일 마감시간
    private String frCloseHoliday; // 휴일 마감시간
    private String frStatWeekday; //  "0" 영수증미표시, "1" 지정 시간 오픈, "2" 상시 오픈, "3" 휴무
    private String frStatSaturday;
    private String frStatHoliday;

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
