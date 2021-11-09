package com.broadwave.toppos.Head.Branoh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-11-09
 * Time :
 * Remark : Toppos 지사 ListDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranohListDto {

    private String brCode; // 지사코드
    private String brName; // 지사명
    private String brContractDt; // 계약일자
    private String brContractFromDt; // 계약기간 from
    private String brContractToDt; // 계약기간 to
    private String brContractState; // 진행중 : 01, 계약완료 : 02
    private Double brCarculateRateHq; // 정산비율(본사)
    private Double brCarculateRateBr; // 정산비율(지사)
    private Double brCarculateRateFr; // 정산비율(가맹점)
    private String brRemark; // 특이사항

    public String getBrCode() {
        return brCode;
    }

    public String getBrName() {
        return brName;
    }

    public StringBuffer getBrContractDt() {
        StringBuffer getBrContractDt = new StringBuffer(brContractDt);
        getBrContractDt.insert(4,'-');
        getBrContractDt.insert(7,'-');
        return getBrContractDt;
    }

    public StringBuffer getBrContractFromDt() {
        StringBuffer getBrContractFromDt = new StringBuffer(brContractFromDt);
        getBrContractFromDt.insert(4,'-');
        getBrContractFromDt.insert(7,'-');
        return getBrContractFromDt;
    }

    public StringBuffer getBrContractToDt() {
        StringBuffer getBrContractToDt = new StringBuffer(brContractToDt);
        getBrContractToDt.insert(4,'-');
        getBrContractToDt.insert(7,'-');
        return getBrContractToDt;
    }

    public String getBrContractState() {
        return brContractState;
    }

    public Double getBrCarculateRateHq() {
        return brCarculateRateHq;
    }

    public Double getBrCarculateRateBr() {
        return brCarculateRateBr;
    }

    public Double getBrCarculateRateFr() {
        return brCarculateRateFr;
    }

    public String getBrRemark() {
        return brRemark;
    }
}
