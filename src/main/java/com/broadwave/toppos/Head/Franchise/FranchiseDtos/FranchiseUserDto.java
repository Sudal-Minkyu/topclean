package com.broadwave.toppos.Head.Franchise.FranchiseDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Double frCarculateRateBr; // 정산비율(지사)
    private Double frCarculateRateFr; // 정산비율(가맹점)
    private Double frRoyaltyRateBr; // 로얄티율(지사)
    private Double frRoyaltyRateFr; // 정산비율(가맹점)

    private Integer frDepositAmount; // 보증금
    private Integer frRentalAmount; // 임대료

    private Integer frEstimateDuration; // 출고예정일
    private String frTagNo; // 가맹점 택번호(3자리)
    private String frRemark; // 특이사항

    private String frBusinessNo; // 사업자번호(10자리)
    private String frRpreName; // 가맹점주이름
    private String frTelNo; // 가맹점 전화번호

    private String frPostNo; // 가맹점 우편번호
    private String frAddress; // 가맹점 주소
    private String frAddressDetail; // 가맹점 상세주소
    private String frMultiscreenYn; // 멀티스크린 사용여부

    private String frOpenWeekday; // 평일 오픈시간
    private String frOpenSaturday; // 토요일 오픈시간
    private String frOpenHoliday; // 휴일 오픈시간
    private String frCloseWeekday; // 평일 마감시간
    private String frCloseSaturday; // 토요일 마감시간
    private String frCloseHoliday; // 휴일 마감시간
    private String frStatWeekday; //  "0" 영수증미표시, "1" 지정 시간 오픈, "2" 상시 오픈, "3" 휴무
    private String frStatSaturday;
    private String frStatHoliday;

    public StringBuffer getFrContractDt() {
        if(frContractDt != null && !frContractDt.equals("")){
            StringBuffer getFrContractDt = new StringBuffer(frContractDt);
            getFrContractDt.insert(4,'-');
            getFrContractDt.insert(7,'-');
            return getFrContractDt;
        }else{
            return null;
        }
    }

    public StringBuffer getFrContractFromDt() {
        if(frContractFromDt != null && !frContractFromDt.equals("")){
            StringBuffer getFrContractFromDt = new StringBuffer(frContractFromDt);
            getFrContractFromDt.insert(4,'-');
            getFrContractFromDt.insert(7,'-');
            return getFrContractFromDt;
        }else{
            return null;
        }
    }

    public StringBuffer getFrContractToDt() {
        if(frContractToDt != null && !frContractToDt.equals("")){
            StringBuffer getFrContractToDt = new StringBuffer(frContractToDt);
            getFrContractToDt.insert(4,'-');
            getFrContractToDt.insert(7,'-');
            return getFrContractToDt;
        }else{
            return null;
        }
    }

}
