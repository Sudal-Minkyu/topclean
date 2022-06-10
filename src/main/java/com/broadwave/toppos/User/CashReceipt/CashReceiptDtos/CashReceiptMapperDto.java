package com.broadwave.toppos.User.CashReceipt.CashReceiptDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * @author Minkyu
 * Date : 2022-04-27
 * Time :
 * Remark : Toppos 현금영수증발행 MapperDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashReceiptMapperDto {

    private String frNo; // 접수코드

    private String fcType; // 현금영수증타입 (1:소비자소득공제, 2:사업자지출증빙, 3: 자진발급)
    private String fcInType; // 입력타입 (01:결제, 02:미수,03:통합결제) -> 01 - 접수화면에서결제시, 02 - 미수처리화면에서결제시, 03 - 통합결제에서 처리시
    private Integer fcRealAmt; // 발행금액

    private String fcCatApprovalno; // 승인번호 ex 73536757
    private String fcCatApprovaltime; // 승인시간 ex 2111241411144
    private String fcCatCardno; // 카드번호 ex 010-****-7785
    private String fcCatIssuercode; // 이슈코드 ex > 01
    private String fcCatIssuername; // 이슈명칭
    private String fcCatMuechantnumber; // 카드가맹점코드 ex > 72729972
    private String fcCatMessage1; // 단말기메세제1 : ex> IBK 비씨카드
    private String fcCatMessage2; // 단말기메세제2 : ex> OK:73536757
    private String fcCatNotice1; // 단말기Notice1 : ex> EDC매출표
    private String fcCatTotamount; // 결제금액 : ex> 000012000
    private String fcCatVatamount; // 부가세금액 : ex> 000001090
    private String fcCatTelegramflagt; // 전문구분 : ex a1

}
