package com.broadwave.toppos.User.ReuqestMoney.Requset.Find;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2022-04-01
 * Time :
 * Remark : Toppos 물건찾기 FindListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindListDto {

    private Long ffId; // 물건찾기 고정값
    private String frName; // 가맹점명
    private String bcName; // 고객명

    private String ffYyyymmdd; // 물건찾기 요청일자
    private String frYyyymmdd; // 접수일자

    private String fdS2Dt; // 입고일
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

    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )
    private String fdState; // 현재상태 ( S1 : 접수, S2: 지사입고,S3 강제출고, S4:가맹점입고, S6: 고객인도, S7: 지사강제출고, S8: 가맹점강제입고)

    private String ffState; // 물품찾기 상태

    private String fdRemark; // 특이사항


    public StringBuffer getFfYyyymmdd() {
        if(ffYyyymmdd != null && !ffYyyymmdd.equals("")){
            StringBuffer getFfYyyymmdd = new StringBuffer(ffYyyymmdd);
            getFfYyyymmdd.insert(4,'-');
            getFfYyyymmdd.insert(7,'-');
            return getFfYyyymmdd;
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

}
