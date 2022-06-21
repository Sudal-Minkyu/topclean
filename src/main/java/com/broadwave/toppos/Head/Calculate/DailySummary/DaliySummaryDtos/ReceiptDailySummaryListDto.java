package com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Minkyu
 * Date : 2022-06-21
 * Time :
 * Remark : Toppos 본사 가맹점별 일정산 입금현황 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDailySummaryListDto {

    private String brName; // 지사명
    private String frName; // 가맹점명

    private BigDecimal total; //  입금한 총합
    private String inamtcnt; //  미입금 건수

    private String amt01; // 1일 입금 확인여부
    private BigDecimal inamt01; // 1일 입금한 금액

    private String amt02; // 2일 입금 확인여부
    private BigDecimal inamt02; // 2일 입금한 금액

    private String amt03; // 3일 입금 확인여부
    private BigDecimal inamt03; // 3일 입금한 금액

    private String amt04; // 4일 입금 확인여부
    private BigDecimal inamt04; // 4일 입금한 금액

    private String amt05; // 5일 입금 확인여부
    private BigDecimal inamt05; // 5일 입금한 금액

    private String amt06; // 6일 입금 확인여부
    private BigDecimal inamt06; // 6일 입금한 금액

    private String amt07; // 7일 입금 확인여부
    private BigDecimal inamt07; // 7일 입금한 금액

    private String amt08; // 8일 입금 확인여부
    private BigDecimal inamt08; // 8일 입금한 금액

    private String amt09; // 9일 입금 확인여부
    private BigDecimal inamt09; // 9일 입금한 금액

    private String amt10; // 10일 입금 확인여부
    private BigDecimal inamt10; // 10일 입금한 금액

    private String amt11; // 11일 입금 확인여부
    private BigDecimal inamt11; // 11일 입금한 금액

    private String amt12; // 12일 입금 확인여부
    private BigDecimal inamt12; // 12일 입금한 금액

    private String amt13; // 13일 입금 확인여부
    private BigDecimal inamt13; // 13일 입금한 금액

    private String amt14; // 14일 입금 확인여부
    private BigDecimal inamt14; // 14일 입금한 금액

    private String amt15; // 15일 입금 확인여부
    private BigDecimal inamt15; // 15일 입금한 금액

    private String amt16; // 16일 입금 확인여부
    private BigDecimal inamt16; // 16일 입금한 금액

    private String amt17; // 17일 입금 확인여부
    private BigDecimal inamt17; // 17일 입금한 금액

    private String amt18; // 18일 입금 확인여부
    private BigDecimal inamt18; // 18일 입금한 금액

    private String amt19; // 19일 입금 확인여부
    private BigDecimal inamt19; // 19일 입금한 금액

    private String amt20; // 20일 입금 확인여부
    private BigDecimal inamt20; // 20일 입금한 금액

    private String amt21; // 21일 입금 확인여부
    private BigDecimal inamt21; // 21일 입금한 금액

    private String amt22; // 22일 입금 확인여부
    private BigDecimal inamt22; // 22일 입금한 금액

    private String amt23; // 23일 입금 확인여부
    private BigDecimal inamt23; // 23일 입금한 금액

    private String amt24; // 24일 입금 확인여부
    private BigDecimal inamt24; // 24일 입금한 금액

    private String amt25; // 25일 입금 확인여부
    private BigDecimal inamt25; // 25일 입금한 금액

    private String amt26; // 26일 입금 확인여부
    private BigDecimal inamt26; // 26일 입금한 금액

    private String amt27; // 27일 입금 확인여부
    private BigDecimal inamt27; // 27일 입금한 금액

    private String amt28; // 28일 입금 확인여부
    private BigDecimal inamt28; // 28일 입금한 금액

    private String amt29; // 29일 입금 확인여부
    private BigDecimal inamt29; // 29일 입금한 금액

    private String amt30; // 30일 입금 확인여부
    private BigDecimal inamt30; // 30일 입금한 금액

    private String amt31; // 31일 입금 확인여부
    private BigDecimal inamt31; // 31일 입금한 금액

}
