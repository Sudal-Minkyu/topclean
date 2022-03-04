package com.broadwave.toppos.Head.Franchise.FranchiseDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-29
 * Time :
 * Remark : Toppos 가맹점전용 나의정보용 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseUserDto {

    private String frCode; // 가맹점코드 3자리
    private String frName; // 가맹점명
    private String frContractDt; // 계약일자
    private String frContractFromDt; // 계약기간 from
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

    private String frPostNo; // 가맹점 우편번호
    private String frAddress; // 가맹점 주소
    private String frAddressDetail; // 가맹점 상세주소
    private String frMultiscreenYn; // 멀티스크린 사용여부

    public StringBuffer getFrContractDt() {
        if(!frContractDt.equals("")){
            StringBuffer getFrContractDt = new StringBuffer(frContractDt);
            getFrContractDt.insert(4,'-');
            getFrContractDt.insert(7,'-');
            return getFrContractDt;
        }else{
            return null;
        }
    }

    public StringBuffer getFrContractFromDt() {
        if(!frContractFromDt.equals("")){
            StringBuffer getFrContractFromDt = new StringBuffer(frContractFromDt);
            getFrContractFromDt.insert(4,'-');
            getFrContractFromDt.insert(7,'-');
            return getFrContractFromDt;
        }else{
            return null;
        }
    }

    public StringBuffer getFrContractToDt() {
        if(!frContractToDt.equals("")){
            StringBuffer getFrContractToDt = new StringBuffer(frContractToDt);
            getFrContractToDt.insert(4,'-');
            getFrContractToDt.insert(7,'-');
            return getFrContractToDt;
        }else{
            return null;
        }
    }

}
