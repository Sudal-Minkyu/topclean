package com.broadwave.toppos.User.CashReceipt.CashReceiptDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-06-10
 * Time :
 * Remark : Toppos 현금영수증발행 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashReceiptDto {

    private String frCode; // 가맹점 코드

    private Long fcId;
    private String fcYyyymmdd; // 등록날짜
    private String fcType; // 현금영수증타입 (1:소비자소득공제, 2:사업자지출증빙, 3: 자진발급)
    private String fcCatApprovalno; // 승인번호 ex 73536757
    private String fcCatApprovaltime; // 승인시간 ex 2111241411144
    private String fcCatTotamount; // 결제금액 : ex> 000012000

}
