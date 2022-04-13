package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-01-05
 * Time :
 * Remark : Toppos 가맹점 접수세부 세탁인도용 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailDeliveryDto {
    private String frRefType; // 접수타입(01:일반, 02:무인보관함, 03:배송APP)
    private String bcName; // 고객이름

    private Long fdId; // 접수세부테이블 고유값 ID

    private String frYyyymmdd; // 접수일자
    private LocalDateTime frInsertDt; // 접수시간
    private String fdTag; // 택번호

    private String fdColor; // 색상코드 (00:미선택 01 흰색 02:검정 03: 회색, 04 빨강 05:주황, 06: 노랑, 07 초록 08 파랑 09:남색 10 보라 11 핑크)
    private String bgName; // 대분류명
    private String bsName; // 중분류명
    private String biName; // 상품명

    private String fdState; // 현재상태 ( S1 : 접수, S2: 지사입고,S3 강제출고, S4:가맹점입고, S6: 고객인도, S7: 지사강제출고, S8: 가맹점강제입고)

    private String fdS2Dt; // 지사입고일
    private String fdS4Dt; // 지사출고일
    private String fdS5Dt; // 가맹점입고일

    private String fdPriceGrade; // 가격등급  1:일반, 2:고급: 3명품 4:아동
    private String fdRetryYn; // 재세탁 여부 (Y  / N) Y 이면 합계금액이 0이다
    private Integer fdPressed; // 다림질 요금
    private Integer fdAdd1Amt; // 추가비용1(접수시점)
    private String fdAdd1Remark; // 추가내용1(접수시점)
    private Integer fdRepairAmt; // 수선금액
    private String fdRepairRemark; // 수선내용
    private Integer fdWhitening; // 표백 요금
    private Integer fdPollution; // 오염 추가요금
    private Integer fdStarch; // 풀먹임 요금
    private Integer fdWaterRepellent; // 발수가공요금
    private String fdUrgentYn; // 급세탁 여부 (Y  / N) 기본값 : N

    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )
    private String fdRemark; // 특이사항

    private String fdEstimateDt; // 출고예정일

    private Integer fdPollutionType;
    private Integer fdPollutionBack;

    public Integer getFdPollutionType() {
        return fdPollutionType + fdPollutionBack;
    }

    public StringBuffer getFdEstimateDt() {
        if(fdEstimateDt != null && !fdEstimateDt.equals("")){
            StringBuffer getFdEstimateDt = new StringBuffer(fdEstimateDt);
            getFdEstimateDt.insert(4,'-');
            getFdEstimateDt.insert(7,'-');
            return getFdEstimateDt;
        }else{
            return null;
        }
    }

    public StringBuffer getFrYyyymmdd() {
        if(frYyyymmdd != null && !frYyyymmdd.equals("")){
            StringBuffer getFrYyyymmdd = new StringBuffer(frYyyymmdd);
            getFrYyyymmdd.insert(4,'-');
            getFrYyyymmdd.insert(7,'-');
            return getFrYyyymmdd;
        }else{
            return null;
        }
    }

    public StringBuffer getFdS4Dt() {
        if(fdS4Dt != null && !fdS4Dt.equals("")){
            StringBuffer getFdS4Dt = new StringBuffer(fdS4Dt);
            getFdS4Dt.insert(4,'-');
            getFdS4Dt.insert(7,'-');
            return getFdS4Dt;
        }else{
            return null;
        }
    }

    public StringBuffer getFdS2Dt() {
        if(fdS2Dt != null && !fdS2Dt.equals("")){
            StringBuffer getFdS2Dt = new StringBuffer(fdS2Dt);
            getFdS2Dt.insert(4,'-');
            getFdS2Dt.insert(7,'-');
            return getFdS2Dt;
        }else{
            return null;
        }
    }

    public StringBuffer getFdS5Dt() {
        if(fdS5Dt != null && !fdS5Dt.equals("")){
            StringBuffer getFdS5Dt = new StringBuffer(fdS5Dt);
            getFdS5Dt.insert(4,'-');
            getFdS5Dt.insert(7,'-');
            return getFdS5Dt;
        }else{
            return null;
        }
    }

}
