package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark : Toppos 가맹점 접수세부 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_request_dtl")
public class RequestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fd_id")
    private Long id; // 접수세부테이블 고유값 ID

    @ManyToOne(targetEntity = Request.class,fetch = FetchType.EAGER)
    @JoinColumn(name="fr_id")
    private Request frId; // 접수마스터 고유값 ID

    @Column(name="fr_no")
    private String frNo; // 접수코드

    @Column(name="fd_tag")
    private String fdTag; // 택번호

    @Column(name="bi_itemcode")
    private String biItemcode; // 상품코드

    @Column(name="fd_state")
    private String fdState; // 현재상태 ( S1 : 접수, S2: 지사입고,S3 지사출고, S4:가맹점입고, S5: 고객인도)

    @Column(name="fd_state_dt")
    private LocalDateTime fdStateDt; // 현재상태변경시간

    @Column(name="fd_pre_state")
    private String fdPreState; // 이전상태  ( S1 : 접수, S2: 지사입고,S3 지사출고, S4:가맹점입고, S5: 고객인도)

    @Column(name="fd_pre_state_dt")
    private LocalDateTime fdPreStateDt; // 이전상태변경시간

    @Column(name="fd_s2_dt")
    private String fdS2Dt; // 지사입고일

    @Column(name="fd_s2_time")
    private LocalDateTime fdS2Time; // '지사입고일시간'

    @Column(name="fd_s2_type")
    private String fdS2Type; // 지사입고방법(01:수기마감, 02:QR자동마감)

    @Column(name="fd_s3_dt")
    private String fdS3Dt; // 지사반송일

    @Column(name="fd_s3_time")
    private LocalDateTime fdS3Time; // 지사반송시간

    @Column(name="fd_s3_id")
    private String fdS3Id; // 지사반송 담장자ID

    @Column(name="fd_s4_dt")
    private String fdS4Dt; // 지사출고일

    @Column(name="fd_s4_time")
    private LocalDateTime fdS4Time; // 지사출고시간

    @Column(name="fd_s4_id")
    private String fdS4Id; // 지사출고 담장자ID

    @Column(name="fd_s5_dt")
    private String fdS5Dt; // 가맹점입고일

    @Column(name="fd_s5_time")
    private LocalDateTime fdS5Time; // 가맹점입고시간

    @Column(name="fd_s6_dt")
    private String fdS6Dt; // 고객인도일

    @Column(name="fd_s6_time")
    private LocalDateTime fdS6Time; // 고객인도시간

    @Column(name="fd_s7_dt")
    private String fdS7Dt; // 지사강제출고일

    @Column(name="fd_s7_id")
    private String fdS5Id; // 지사강제출고 담장자ID

    @Column(name="fd_s7_time")
    private LocalDateTime fdS7Time; // 지사강제출고시간

    @Column(name="fd_s8_dt")
    private String fdS8Dt; // 가맹점강제입고일

    @Column(name="fd_s8_time")
    private LocalDateTime fdS8Time; // 가맹점강제입고시간

    @Column(name="fd_cancel")
    private String fdCancel; // 접수취소 Y 기본값 N

    @Column(name="fd_cacel_dt")
    private LocalDateTime fdCacelDt; // 접수취소 시간

    @Column(name="fd_color")
    private String fdColor; // 색상코드 (00:미선택 01 흰색 02:검정 03: 회색, 04 빨강 05:주황, 06: 노랑, 07 초록 08 파랑 09:남색 10 보라 11 핑크)

    @Column(name="fd_pattern")
    private String fdPattern; // 패턴 (00: 미선택 , 01:체크, 02:혼합, 03: 줄)

    @Column(name="fd_price_grade")
    private String fdPriceGrade; // 가격등급  1:일반, 2:고급: 3명품 4:아동

    @Column(name="fd_origin_amt")
    private Integer fdOriginAmt; // 최초 정상금액(일반상태일경우)

    @Column(name="fd_normal_amt")
    private Integer fdNormalAmt; // 정상금액

    @Column(name="fd_repair_remark")
    private String fdRepairRemark; // 수선내용

    @Column(name="fd_repair_amt")
    private Integer fdRepairAmt; // 수선금액

    @Column(name="fd_add1_remark")
    private String fdAdd1Remark; // 추가내용1(접수시점)

    @Column(name="fd_special_yn")
    private String fdSpecialYn; // 특수여부(접수시점)

    @Column(name="fd_add1_amt")
    private Integer fdAdd1Amt; // 추가비용1(접수시점)

    @Column(name="fd_add2_remark")
    private String fdAdd2Remark; // 추가내용2(검품후 추가비용 발생)

    @Column(name="fd_add2_amt")
    private Integer fdAdd2Amt; // 추가비용2(검품후 추가비용 발생)

    @Column(name="fd_pressed")
    private Integer fdPressed; // 다림질 요금

    @Column(name="fd_whitening")
    private Integer fdWhitening; // 표백 요금

    @Column(name="fd_pollution")
    private Integer fdPollution; // 오염 추가요금

    @Column(name="fd_pollution_level")
    private Integer fdPollutionLevel; // 오염 선택레벨( 1~5)

    @Column(name="fd_starch")
    private Integer fdStarch; // 풀먹임 요금

    @Column(name="fd_water_repellent")
    private Integer fdWaterRepellent; // 발수가공요금

    @Column(name="fd_discount_grade")
    private String fdDiscountGrade; // 할인등급 1:일반(할인0%), 2: vip, 3.vvip

    @Column(name="fd_discount_amt")
    private Integer fdDiscountAmt; // 할인금액

    @Column(name="fd_qty")
    private Integer fdQty; // 수량

    @Column(name="fd_request_amt")
    private Integer fdRequestAmt; // 접수금액( (정상 + 수선 + 추가1 -할인) * 수량 )

    @Column(name="fd_retry_yn")
    private String fdRetryYn; // 재세탁 여부 (Y  / N) Y 이면 아래 합계금액이 0이다

    @Column(name="fd_urgent_yn")
    private String fdUrgentYn; // 급세탁 여부 (Y  / N) 기본값 : N

    @Column(name="fd_tot_amt")
    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )

    @Column(name="fd_remark")
    private String fdRemark; // 특이사항

    @Column(name="fd_estimate_dt")
    private String fdEstimateDt; // 출고예정일

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insert_date;

    @Column(name="modify_id")
    private String modity_id;

    @Column(name="modify_date")
    private LocalDateTime modity_date;

}
