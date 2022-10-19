package com.broadwave.toppos.User.UserDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark : 가맹점 접수페이지 Etc데이터
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtcDataDto {

    private String frCode; // 가맹점코드 3자리
    private String frName; // 가맹점명
    private String fdTag; // 태그번호
    private String frTagNo; // 가맹점 택번호 3자리
    private String frEstimateDate; // 출고예정일
    private String frBusinessNo; // 사업자번호(10자리)
    private String frRpreName; // 가맹점주이름
    private String frTelNo; // 가맹점 전화번호
    private String frMultiscreenYn; // 멀티스크린 사용여부
    private String frUrgentDayYn; // 가맹점 당일세탁 사용여부

    private String frManualPromotionYn; // 본사용 수동 프로모션 할인기능 사용여부
    private String frCardTid; // 카드단말기 Tid

    public StringBuffer getFrEstimateDate() {
        if(frEstimateDate != null && !frEstimateDate.equals("")){
            StringBuffer frEstimateDt = new StringBuffer(frEstimateDate);
            frEstimateDt.insert(4,'-');
            frEstimateDt.insert(7,'-');
            return frEstimateDt;
        }else{
            return null;
        }
    }
}
