package com.broadwave.toppos.Head.Franohise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-29
 * Time :
 * Remark : Toppos 가맹점전용 나의정보용 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchisUserDto {

    private String frCode; // 가맹점코드 3자리
    private String frName; // 가맹점명
    private String frContractDt; // 계약일자
    private String brContractFromDt; // 계약기간 from
    private String frContractToDt; // 계약기간 to

    private String brName; // 배정지사명

    private Double brCarculateRateHq; // 정산비율 본사
    private Double brCarculateRateBr; // 정산비율 지사
    private Double brCarculateRateFr; // 정산비율 가맹점

    private Integer frEstimateDuration; // 출고예정일
    private String frTagNo; // 가맹점 택번호(3자리)

    private String frBusinessNo; // 사업자번호(10자리)
    private String frRpreName; // 가맹점주이름
    private String frTelNo; // 가맹점 전화번호

    public String getFrBusinessNo() {
        return frBusinessNo;
    }

    public String getFrRpreName() {
        return frRpreName;
    }

    public String getFrTelNo() {
        return frTelNo;
    }

    public Integer getFrEstimateDuration() {
        return frEstimateDuration;
    }

    public String getFrTagNo() {
        return frTagNo;
    }

    public String getBrContractFromDt() {
        return brContractFromDt;
    }

    public String getFrCode() {
        return frCode;
    }

    public String getFrName() {
        return frName;
    }

    public StringBuffer getFrContractDt() {
        StringBuffer getFrContractDt = new StringBuffer(frContractDt);
        getFrContractDt.insert(4,'-');
        getFrContractDt.insert(7,'-');
        return getFrContractDt;
    }

    public StringBuffer getFrContractFromDt() {
        StringBuffer getFrContractFromDt = new StringBuffer(brContractFromDt);
        getFrContractFromDt.insert(4,'-');
        getFrContractFromDt.insert(7,'-');
        return getFrContractFromDt;
    }

    public StringBuffer getFrContractToDt() {
        StringBuffer getFrContractToDtDate = new StringBuffer(frContractToDt);
        getFrContractToDtDate.insert(4,'-');
        getFrContractToDtDate.insert(7,'-');
        return getFrContractToDtDate;
    }


    public String getBrName() {
        return brName;
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
}