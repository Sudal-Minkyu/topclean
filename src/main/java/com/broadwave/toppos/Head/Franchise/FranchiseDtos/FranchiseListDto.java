package com.broadwave.toppos.Head.Franchise.FranchiseDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-11-09
 * Time :
 * Remark : Toppos 가맹점 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseListDto {

    private String frCode; // 가맹점코드 3자리
    private String frName; // 가맹점명
    private String frRefCode; // 가맹점관리코드
    private String frContractDt; // 계약일자
    private String brContractFromDt; // 계약기간 from
    private String frContractToDt; // 계약기간 to
    private String frContractState; // 진행중 : 01, 계약완료 : 02
    private String brAssignState; // 지사 배정상태 미배정: 01, 배정완료: 02
    private String frPriceGrade; // 가격등급 A,B,C,D,E
    private String frTagNo; // 가맹점 택번호 3자리
    private String frTagType; // 2: 2자리, 3: 3자리
    private Integer frEstimateDuration; // 출고예정일
    private String frRemark; // 특이사항
    private String brName; // 배정지사명

    private String frBusinessNo; // 사업자번호(10자리)
    private String frRpreName; // 가맹점주이름
    private String frTelNo; // 가맹점 전화번호

    private String frPostNo; // 가맹점 우편번호
    private String frAddress; // 가맹점 주소
    private String frAddressDetail; // 가맹점 상세주소

    private Double frCarculateRateBr; // 정산비율(지사)
    private Double frCarculateRateFr; // 정산비율(가맹점)
    private Double frRoyaltyRateBr; // 로얄티율(지사)
    private Double frRoyaltyRateFr; // 정산비율(가맹점)

    private String frUrgentDayYn; // 가맹점 당일세탁 사용여부

    private String frManualPromotionYn; // 본사용 수동 프로모션 할인기능 사용여부
    private String frCardTid; // 카드단말기 Tid

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
        if(brContractFromDt != null && !brContractFromDt.equals("")){
            StringBuffer getFrContractFromDt = new StringBuffer(brContractFromDt);
            getFrContractFromDt.insert(4,'-');
            getFrContractFromDt.insert(7,'-');
            return getFrContractFromDt;
        }else{
            return null;
        }
    }

    public StringBuffer getFrContractToDt() {
        if(frContractToDt != null && !frContractToDt.equals("")){
            StringBuffer getFrContractToDtDate = new StringBuffer(frContractToDt);
            getFrContractToDtDate.insert(4,'-');
            getFrContractToDtDate.insert(7,'-');
            return getFrContractToDtDate;
        }else{
            return null;
        }
    }

}
