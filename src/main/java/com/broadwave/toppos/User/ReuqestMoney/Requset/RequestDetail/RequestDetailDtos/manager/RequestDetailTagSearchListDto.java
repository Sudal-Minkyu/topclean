package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-02-15
 * Time :
 * Remark : Toppos 지사 택번호조회 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailTagSearchListDto {

    private String frName; // 가맹점명
    private String frRefType; // 접수타입(구분)

    private String frYyyymmdd; // 접수일자
    private String fdS2Dt; // 입고일자
    private String fdS4Dt; // 출고일자

    private String fdTag; // 택번호
    private String fdColor; // 색상코드 (00:미선택 01 흰색 02:검정 03: 회색, 04 빨강 05:주황, 06: 노랑, 07 초록 08 파랑 09:남색 10 보라 11 핑크)

    private String bgName; // 대분류명
    private String bsName; // 중분류명
    private String biName; // 상품명

    private String fdPriceGrade; // 가격등급  1:일반, 2:고급: 3명품 4:아동
    private String fdRetryYn; // 재세탁 여부 (Y  / N) Y 이면 합계금액이 0이다
    private Integer fdPressed; // 다림질 요금
    private Integer fdAdd1Amt; // 추가비용1(접수시점)
    private String fdAdd1Remark; // 추가내용1(접수시점)
    private Integer fdRepairAmt; // 수선금액
    private String fdRepairRemark; // 수선내용
    private Integer fdWhitening; // 표백 요금
    private Integer fdPollution; // 오염 추가요금
    private Integer fdWaterRepellent; // 발수가공요금
    private Integer fdStarch; // 풀먹임 요금
    private String fdUrgentYn; // 급세탁 여부 (Y  / N) 기본값 : N

    private String bcName; // 고객명
    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )
    private String fdState; // 현재상태 ( S1 : 접수, S2: 지사입고,S3 지사출고, S4:가맹점입고, S5: 고객인도)

    private String fdRemark; // 특이사항
    private String fdS3Dt; // 지사반송일
    private String fdS7Dt; // 지사강제출고일
    private String fdS8Dt; // 가맹점강제입고일

    public StringBuffer getFrYyyymmdd() {
        if(frYyyymmdd != null){
            StringBuffer getFrYyyymmdd = new StringBuffer(frYyyymmdd);
            getFrYyyymmdd.insert(4,'-');
            getFrYyyymmdd.insert(7,'-');
            return getFrYyyymmdd;
        }else{
            return null;
        }
    }

    public StringBuffer getFdS4Dt() {
        if(fdS4Dt != null){
            StringBuffer getFdS4Dt = new StringBuffer(fdS4Dt);
            getFdS4Dt.insert(4,'-');
            getFdS4Dt.insert(7,'-');
            return getFdS4Dt;
        }else{
            return null;
        }
    }

    public StringBuffer getFdS2Dt() {
        if(fdS2Dt != null){
            StringBuffer getFdS2Dt = new StringBuffer(fdS2Dt);
            getFdS2Dt.insert(4,'-');
            getFdS2Dt.insert(7,'-');
            return getFdS2Dt;
        }else{
            return null;
        }
    }

    public StringBuffer getFdS3Dt() {
        if(fdS3Dt != null){
            StringBuffer getFdS3Dt = new StringBuffer(fdS3Dt);
            getFdS3Dt.insert(4,'-');
            getFdS3Dt.insert(7,'-');
            return getFdS3Dt;
        }else{
            return null;
        }
    }

    public StringBuffer getFdS7Dt() {
        if(fdS7Dt != null){
            StringBuffer getFdS7Dt = new StringBuffer(fdS7Dt);
            getFdS7Dt.insert(4,'-');
            getFdS7Dt.insert(7,'-');
            return getFdS7Dt;
        }else{
            return null;
        }
    }

    public StringBuffer getFdS8Dt() {
        if(fdS8Dt != null){
            StringBuffer getFdS8Dt = new StringBuffer(fdS8Dt);
            getFdS8Dt.insert(4,'-');
            getFdS8Dt.insert(7,'-');
            return getFdS8Dt;
        }else{
            return null;
        }
    }
}