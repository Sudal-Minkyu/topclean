package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2021-01-05
 * Time :
 * Remark : Toppos 가맹점 접수세부 통합조회용 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailSearchDto {
    // 마스터 테이블에서 가져 올 데이터
    private String bcName; // 고객이름
    private String frYyyymmdd; // 접수일자

    // 세부 테이블에서 가져 올 데이터
    private Long fdId; // 접수세부테이블 고유값 ID
    private Long frId; // 접수마스터 고유값 ID

    private String fdTag; // 택번호
    private String biItemcode; // 상품코드
    private String fdState; // 현재상태 ( S1 : 접수, S2: 지사입고,S3 지사출고, S4:가맹점입고, S5: 고객인도)

//    private LocalDateTime fdStateDt; // 현재상태변경시간 = d

    private String fdS2Dt; // 지사입고일
    private String fdS3Dt; // 지사출고일
    private String fdS4Dt; // 가맹점입고일
    private String fdS5Dt; // 고객인도일

//    private LocalDateTime fdPreStateDt; // 이전상태변경시간 = d

    private String fdCancel; // 접수취소 Y 기본값 N
    private LocalDateTime fdCacelDt; // 접수취소 시간

    private String fdColor; // 색상코드 (00:미선택 01 흰색 02:검정 03: 회색, 04 빨강 05:주황, 06: 노랑, 07 초록 08 파랑 09:남색 10 보라 11 핑크)
    private String fdPattern; // 패턴 (00: 미선택 , 01:체크, 02:혼합, 03: 줄)
    private String fdPriceGrade; // 가격등급  1:일반, 2:고급: 3명품 4:아동

    private Integer fdOriginAmt; // 최초 정상금액(일반상태일경우) ??
    private Integer fdNormalAmt; // 접수금액

    private Integer fdAdd2Amt; // 추가비용2(검품후 추가비용 발생)
    private String fdAdd2Remark; // 추가내용2(검품후 추가비용 발생)

    private Integer fdPollution; // 오염 추가요금
    private String fdDiscountGrade; // 할인등급 1:일반(할인0%), 2: vip, 3.vvip
    private Integer fdDiscountAmt; // 할인금액
    private Integer fdQty; // 수량

    private Integer fdRequestAmt; // 접수금액( (정상 + 수선 + 추가1 -할인) * 수량 )
    private String fdSpecialYn; // 특수여부(접수시점)
    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )
    private String fdRemark; // 특이사항
    private String fdEstimateDt; // 출고예정일

    private String fdRetryYn; // 재세탁 여부 (Y  / N) Y 이면 합계금액이 0이다
    private Integer fdPressed; // 다림질 요금
    private Integer fdAdd1Amt; // 추가비용1(접수시점)
    private String fdAdd1Remark; // 추가내용1(접수시점)
    private Integer fdRepairAmt; // 수선금액
    private String fdRepairRemark; // 수선내용
    private Integer fdWhitening; // 표백 요금
    private Integer fdPollutionLevel; // 오염 선택레벨(0~5)
    private Integer fdWaterRepellent; // 발수가공요금
    private Integer fdStarch; // 풀먹임 요금

    public String getBcName() {
        return bcName;
    }

    public String getFrYyyymmdd() {
        return frYyyymmdd;
    }

    public Long getFdId() {
        return fdId;
    }

    public Long getFrId() {
        return frId;
    }

    public String getFdTag() {
        return fdTag;
    }

    public String getFdS2Dt() {
        return fdS2Dt;
    }

    public String getFdS3Dt() {
        return fdS3Dt;
    }

    public String getFdS4Dt() {
        return fdS4Dt;
    }

    public String getFdS5Dt() {
        return fdS5Dt;
    }

    public String getFdCancel() {
        return fdCancel;
    }

    public String getFdCacelDt() {
        if(fdCacelDt != null){
            return fdCacelDt.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }else{
            return null;
        }
    }

    public String getFdColor() {
        return fdColor;
    }

    public String getFdPattern() {
        return fdPattern;
    }

    public String getFdPriceGrade() {
        return fdPriceGrade;
    }

    public Integer getFdOriginAmt() {
        return fdOriginAmt;
    }

    public Integer getFdNormalAmt() {
        return fdNormalAmt;
    }

    public Integer getFdAdd2Amt() {
        return fdAdd2Amt;
    }

    public String getFdAdd2Remark() {
        return fdAdd2Remark;
    }

    public Integer getFdPollution() {
        return fdPollution;
    }

    public String getFdDiscountGrade() {
        return fdDiscountGrade;
    }

    public Integer getFdDiscountAmt() {
        return fdDiscountAmt;
    }

    public Integer getFdQty() {
        return fdQty;
    }

    public Integer getFdRequestAmt() {
        return fdRequestAmt;
    }

    public String getFdSpecialYn() {
        return fdSpecialYn;
    }

    public Integer getFdTotAmt() {
        return fdTotAmt;
    }

    public String getFdRemark() {
        return fdRemark;
    }

    public String getFdEstimateDt() {
        return fdEstimateDt;
    }

    public String getFdRetryYn() {
        return fdRetryYn;
    }

    public Integer getFdPressed() {
        return fdPressed;
    }

    public Integer getFdAdd1Amt() {
        return fdAdd1Amt;
    }

    public String getFdAdd1Remark() {
        return fdAdd1Remark;
    }

    public Integer getFdRepairAmt() {
        return fdRepairAmt;
    }

    public String getFdRepairRemark() {
        return fdRepairRemark;
    }

    public Integer getFdWhitening() {
        return fdWhitening;
    }

    public Integer getFdPollutionLevel() {
        return fdPollutionLevel;
    }

    public Integer getFdWaterRepellent() {
        return fdWaterRepellent;
    }

    public Integer getFdStarch() {
        return fdStarch;
    }

    public String getBiItemcode() {
        return biItemcode;
    }
}
