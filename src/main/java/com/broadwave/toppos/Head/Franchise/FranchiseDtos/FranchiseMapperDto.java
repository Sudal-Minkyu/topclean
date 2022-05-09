package com.broadwave.toppos.Head.Franchise.FranchiseDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark : Toppos 가맹점 MapperDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseMapperDto {

    private String frCode; // 가맹점코드 3자리
    private String frName; // 가맹점명
    private String frRefCode; // 가맹점관리코드
    private String frTagNo; // 가맹점 택번호
    private String frTagType; // 2: 2자리, 3: 3자리
    private String frContractDt; // 계약일자
    private String frContractFromDt; // 계약기간 from
    private String frContractToDt; // 계약기간 to
    private String frContractState; // 진행중 : 01, 계약완료 : 02
    private String frPriceGrade; // 가격등급 A,B,C,D,E
    private Integer frEstimateDuration; // 출고예정일
    private String frRemark; // 특이사항
    private String frBusinessNo; // 사업자번호(10자리)
    private String frRpreName; // 가맹점주이름
    private String frTelNo; // 가맹점 전화번호

    private Integer frDepositAmount; // 보증금
    private Integer frRentalAmount; // 임대료

    private Double frCarculateRateBr; // 정산비율(지사)
    private Double frCarculateRateFr; // 정산비율(가맹점)
    private Double frRoyaltyRateBr; // 로얄티율(지사)
    private Double frRoyaltyRateFr; // 정산비율(가맹점)

    private String frUrgentDayYn; // 가맹점 당일세탁 사용여부

    private String frPostNo; // 가맹점 우편번호
    private String frAddress; // 가맹점 주소
    private String frAddressDetail; // 가맹점 상세주소

    public String getFrContractDt() {
        if(frContractDt != null && !frContractDt.equals("")){
            String getFrContractDt = frContractDt;
            getFrContractDt = getFrContractDt.replaceAll("-","");
            return getFrContractDt;
        }else{
            return null;
        }
    }

    public String getFrContractFromDt() {
        if(frContractFromDt != null && !frContractFromDt.equals("")){
            String getFrContractFromDt = frContractFromDt;
            getFrContractFromDt = getFrContractFromDt.replaceAll("-","");
            return getFrContractFromDt;
        }else{
            return null;
        }
    }

    public String getFrContractToDt() {
        if(frContractToDt != null && !frContractToDt.equals("")){
            String getFrContractToDt = frContractToDt;
            getFrContractToDt = getFrContractToDt.replaceAll("-","");
            return getFrContractToDt;
        }else{
            return null;
        }
    }

}
