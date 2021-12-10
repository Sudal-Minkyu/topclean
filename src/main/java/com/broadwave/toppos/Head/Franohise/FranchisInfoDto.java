package com.broadwave.toppos.Head.Franohise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-11-15
 * Time :
 * Remark : Toppos 가맹점 InfoDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchisInfoDto {

    private String frCode; // 가맹점코드 3자리
    private String frName; // 가맹점명
    private String frContractDt; // 계약일자
    private String brContractFromDt; // 계약기간 from
    private String frContractToDt; // 계약기간 to
    private String frContractState; // 진행중 : 01, 계약완료 : 02
    private String frContractStateValue; // 지사 배정상태 미배정: 01, 배정완료: 02
    private String brAssignState; // 지사 배정상태 미배정: 01, 배정완료: 02
    private String brCode; // 배정코드
    private String brName; // 배정지사명
    private Double brCarculateRateHq; // 정산비율 본사
    private Double brCarculateRateBr; // 정산비율 지사
    private Double brCarculateRateFr; // 정산비율 가맹점

    private Integer frEstimateDuration; // 출고예정일
    private String frLastTagno; // 가맹점 태그번호

    public Integer getFrEstimateDuration() {
        return frEstimateDuration;
    }

    public String getFrLastTagno() {
        return frLastTagno;
    }

    public String getBrContractFromDt() {
        return brContractFromDt;
    }

    public String getFrContractStateValue() {
        if(frContractStateValue.equals("01")){
            return "미배정";
        }else{
            return "배정완료";
        }
    }

    public String getBrAssignState() {
        return brAssignState;
    }

    public String getFrCode() {
        return frCode;
    }

    public StringBuffer getFrContractDt() {
        StringBuffer getFrContractDt = new StringBuffer(frContractDt);
        getFrContractDt.insert(4,'-');
        getFrContractDt.insert(7,'-');
        return getFrContractDt;
    }

    public String getFrContractState() {
        return frContractState;
    }


    public String getFrName() {
        return frName;
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

    public String getBrCode() {
        return brCode;
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
