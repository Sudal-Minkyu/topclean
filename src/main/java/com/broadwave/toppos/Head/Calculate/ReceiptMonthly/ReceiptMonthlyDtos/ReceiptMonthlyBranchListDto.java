package com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthlyDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Minkyu
 * Date : 2022-06-17
 * Time :
 * Remark : Toppos 본사 지사별 월정산 입금현황 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptMonthlyBranchListDto {

    private String brName; // 지사명

    private String amt01; // 1월 입금 확인여부
    private BigDecimal inamt01; // 1월 입금한 금액

    private String amt02; // 2월 입금 확인여부
    private BigDecimal inamt02; // 2월 입금한 금액

    private String amt03; // 3월 입금 확인여부
    private BigDecimal inamt03; // 3월 입금한 금액

    private String amt04; // 4월 입금 확인여부
    private BigDecimal inamt04; // 4월 입금한 금액

    private String amt05; // 5월 입금 확인여부
    private BigDecimal inamt05; // 5월 입금한 금액

    private String amt06; // 6월 입금 확인여부
    private BigDecimal inamt06; // 6월 입금한 금액

    private String amt07; // 7월 입금 확인여부
    private BigDecimal inamt07; // 7월 입금한 금액

    private String amt08; // 8월 입금 확인여부
    private BigDecimal inamt08; // 8월 입금한 금액

    private String amt09; // 9월 입금 확인여부
    private BigDecimal inamt09; // 9월 입금한 금액

    private String amt10; // 10월 입금 확인여부
    private BigDecimal inamt10; // 10월 입금한 금액

    private String amt11; // 11월 입금 확인여부
    private BigDecimal inamt11; // 11월 입금한 금액

    private String amt12; // 12월 입금 확인여부
    private BigDecimal inamt12; // 12월 입금한 금액

    private BigDecimal total; //  입금한 총합

}
