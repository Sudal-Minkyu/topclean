package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.user;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2022-01-19
 * Time :
 * Remark : Toppos 가맹점 미수관리 페이지의 고객선택 후 리스트 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestCustomerUnCollectDto {

    private Long frId;

    private String frYyyymmdd; // 접수일자
    private Long requestDetailCount; // 건수

    private String bgName; // 대분류명
    private String bsName; // 중분류명
    private String biName; // 상품명

    private Integer frTotalAmount; // 합계금액
    private Integer frPayAmount; // 결제금액

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
