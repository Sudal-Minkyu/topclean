package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.Manager.Process.Issue.Issue;
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
    private String fdState; // 현재상태 ( S1 : 접수, S2: 지사입고,S3 강제출고, S4:가맹점입고, S6: 고객인도, S7: 지사강제출고, S8: 가맹점강제입고)

    @Column(name="fd_state_dt")
    private LocalDateTime fdStateDt; // 현재상태변경시간

    @Column(name="fd_pre_state")
    private String fdPreState; // 이전상태 ( S1 : 접수, S2: 지사입고,S3 강제출고, S4:가맹점입고, S6: 고객인도, S7: 지사강제출고, S8: 가맹점강제입고)

    @Column(name="fd_pre_state_dt")
    private LocalDateTime fdPreStateDt; // 이전상태변경시간

    @Column(name="fd_fr_state")
    private String fdFrState; // 가맹점프로그램 에서변경한최종상태

    @Column(name="fd_fr_state_time")
    private LocalDateTime fdFrStateTime; // 가맹점프로그램 에서변경한최종상태변경일시

    @Column(name="fd_br_state")
    private String fdBrState; // 지사프로그램 에서변경한최종상태

    @Column(name="fd_br_state_time")
    private LocalDateTime fdBrStateTime; // 지사프로그램 에서변경한최종상태변경일시

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

    @Column(name="fd_s4_dt")
    private String fdS4Dt; // 지사출고일

    @Column(name="fd_s4_type")
    private String fdS4Type; // 지사출고타입(01:일반출고, 02:강제출고, 03:가맹점강제입고출고)

    @ManyToOne(targetEntity = Issue.class,fetch = FetchType.EAGER)
    @JoinColumn(name="mi_id")
    private Issue miId; // 지사출고 처리 마스터테이블ID

    @Column(name="fd_s4_time")
    private LocalDateTime fdS4Time; // 지사출고시간

    @Column(name="fd_s4_id")
    private String fdS4Id; // 지사출고 담장자ID

    @Column(name="fd_s5_dt")
    private String fdS5Dt; // 가맹점입고일

    @Column(name="fd_s5_time")
    private LocalDateTime fdS5Time; // 가맹점입고시간

    @Column(name="fd_s6_type")
    private String fdS6Type; // 고객인도타입(01:일반인도, 02:반품인도(강제출고건))

    @Column(name="fd_s6_dt")
    private String fdS6Dt; // 고객인도일

    @Column(name="fd_s6_time")
    private LocalDateTime fdS6Time; // 고객인도시간

    @Column(name="fd_s6_cancel_yn")
    private String fdS6CancelYn; // 고객인도취소이력 (취소할경우 'Y'로 표기, 기본값 N)

    @Column(name="fd_s6_cancel_time")
    private LocalDateTime fdS6CancelTime; // 고객인도시간 취소시 시간기록

    @Column(name="fd_s7_type")
    private String fdS7Type; // 01:강제출고, 02:반품출고

    @Column(name="fd_s7_dt")
    private String fdS7Dt; // 지사강제출고일

    @Column(name="fd_s7_id")
    private String fdS7Id; // 지사강제출고 담장자ID

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
    private Integer fdPollutionLevel; // 오염 선택레벨(1~5)

    @Column(name="fd_pollution_loc_fcn")
    private String fdPollutionLocFcn; // 오염위치 앞 - 목  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_fcs")
    private String fdPollutionLocFcs; // 오염위치 앞 - 배  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_fcb")
    private String fdPollutionLocFcb; // 오염위치 앞 - 하의  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_flh")
    private String fdPollutionLocFlh; // 오염위치 앞 - 왼손  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_frh")
    private String fdPollutionLocFrh; // 오염위치 앞 - 오른손  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_flf")
    private String fdPollutionLocFlf; // 오염위치 앞 - 왼발  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_frf")
    private String fdPollutionLocFrf; // 오염위치 앞 - 오른발  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_bcn")
    private String fdPollutionLocBcn; // 오염위치 뒤 - 목  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_bcs")
    private String fdPollutionLocBcs; // 오염위치 뒤 - 배  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_bcb")
    private String fdPollutionLocBcb; // 오염위치 뒤 - 하의  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_brh")
    private String fdPollutionLocBrh; // 오염위치 뒤 - 오른손  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_blh")
    private String fdPollutionLocBlh; // 오염위치 뒤 - 왼손  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_brf")
    private String fdPollutionLocBrf; // 오염위치 뒤 - 오른발  체크면 Y 아니면 N

    @Column(name="fd_pollution_loc_blf")
    private String fdPollutionLocBlf; // 오염위치 뒤 - 왼발  체크면 Y 아니면 N

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

    @Column(name="fd_agree_type")
    private String fdAgreeType; // 동의타입 (온라인 : 1, 서면 : 2)

    @Column(length = 100000, name="fd_sign_image")
    private String fdSignImage; // 운동화 세탁 동의사인이미지 Blob객체사용

    @Column(name="fd_request_amt")
    private Integer fdRequestAmt; // 접수금액( (정상 + 수선 + 추가1 -할인) * 수량 )

    @Column(name="fd_retry_yn")
    private String fdRetryYn; // 재세탁 여부 (Y  / N) Y 이면 아래 합계금액이 0이다

    @Column(name="fd_urgent_yn")
    private String fdUrgentYn; // 급세탁 여부 (Y  / N) 기본값 : N

    @Column(name="fd_urgent_type")
    private String fdUrgentType; // 급세탁 타입(1: 당일세탁, 2: 특급세탁(1박2일), 3급세탁(2박3일))

    @Column(name="fd_urgent_amt")
    private Integer fdUrgentAmt; // 급세탁 추가비용

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
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modify_date;



}
