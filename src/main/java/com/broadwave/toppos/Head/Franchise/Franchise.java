package com.broadwave.toppos.Head.Franchise;

import com.broadwave.toppos.Head.Branch.Branch;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark : Toppos 가맹점 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Transactional
@Table(name="bs_franchise")
public class Franchise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fr_id")
    private Long id;

    @Column(unique = true,name="fr_code")
    private String frCode; // 가맹점코드 3자리

    @Column(name="fr_name")
    private String frName; // 가맹점명

    @Column(name="fr_ref_code")
    private String frRefCode; // 가맹점관리코드

    @Column(name="fr_contract_dt")
    private String frContractDt; // 계약일자

    @Column(name="fr_contract_from_dt")
    private String frContractFromDt; // 계약기간 from

    @Column(name="fr_contract_to_dt")
    private String frContractToDt; // 계약기간 to

    @Column(name="fr_contract_state")
    private String frContractState; // 진행중 : 01, 계약완료 : 02

    @Column(name="fr_price_grade")
    private String frPriceGrade; // 가격등급 A,B,C,D,E

    @Column(name="fr_tag_no")
    private String frTagNo; // 가맹점 택번호 2자리 or 3자리

    @Column(name="fr_tag_type")
    private String frTagType; // 2: 2자리, 3: 3자리

    @Column(name="fr_estimate_duration")
    private Integer frEstimateDuration; // 출고예정일

    @Column(name="fr_business_no")
    private String frBusinessNo; // 사업자번호(10자리)

    @Column(name="fr_rpre_name")
    private String frRpreName; // 가맹점주이름

    @Column(name="fr_tel_no")
    private String frTelNo; // 가맹점 전화번호

    @Column(name="fr_post_no")
    private String frPostNo; // 가맹점 우편번호

    @Column(name="fr_address")
    private String frAddress; // 가맹점 주소

    @Column(name="fr_address_detail")
    private String frAddressDetail; // 가맹점 상세주소

    @Column(name="fr_multiscreen_yn")
    private String frMultiscreenYn; // 멀티스크린 사용여부

    @Column(name="fr_remark")
    private String frRemark; // 특이사항

    @Column(name="fr_deposit_amount")
    private Integer frDepositAmount; // 보증금

    @Column(name="fr_rental_amount")
    private Integer frRentalAmount; // 임대료

    @ManyToOne(targetEntity = Branch.class,fetch = FetchType.EAGER)
    @JoinColumn(name="br_id")
    private Branch brId; // 배정된 지사 ID

    @Column(name="br_code")
    private String brCode; // 배정된 지사코드

    @Column(name="br_assign_state")
    private String brAssignState; // 지사 배정상태 미배정: 01, 배정완료: 02

    @Column(name="carculate_rate_br")
    private Double frCarculateRateBr; // 정산비율(지사)

    @Column(name="carculate_rate_fr")
    private Double frCarculateRateFr; // 정산비율(가맹점)

    @Column(name="royalty_rate_br")
    private Double frRoyaltyRateBr; // 로얄티율(지사)

    @Column(name="royalty_rate_fr")
    private Double frRoyaltyRateFr; // 정산비율(가맹점)

    @Column(name="fr_last_tagno")
    private String frLastTagno; // 가맹점 태그번호

    @Column(name="ft_last_tagno_modify_no")
    private String ftLastTagnoModifyNo; // 마지막변경한 택번호저장(비번입력하여 강제변경시)

    @Column(name="ft_last_tagno_modify_id")
    private String ftLastTagnoModify_id; // 택번호변경자 ID(비번입력하여강제변경시에만)

    @Column(name="ft_last_tagno_modify_dt")
    private LocalDateTime ftLastTagnoModifyDt; // 택번호변경시간(비번입력하여강제변경시에만)

    @Column(name="fr_urgent_day_yn")
    private String frUrgentDayYn; // 가맹점 당일세탁 사용여부

    @Column(name="fr_manual_promotion_yn")
    private String frManualPromotionYn; // 본사용 수동 프로모션 할인기능 사용여부

    @Column(name="fr_card_tid")
    private String frCardTid; // 카드단말기 Tid

    @Column(name="fr_open_weekday")
    private String frOpenWeekday; // 평일 오픈시간

    @Column(name="fr_open_saturday")
    private String frOpenSaturday; // 토요일 오픈시간

    @Column(name="fr_open_holiday")
    private String frOpenHoliday; // 휴일 오픈시간

    @Column(name="fr_close_weekday")
    private String frCloseWeekday; // 평일 마감시간

    @Column(name="fr_close_saturday")
    private String frCloseSaturday; // 토요일 마감시간

    @Column(name="fr_close_holiday")
    private String frCloseHoliday; // 휴일 마감시간

    @Column(name="fr_stat_weekday")
    private String frStatWeekday; //  "0" 영수증미표시, "1" 지정 시간 오픈, "2" 상시 오픈, "3" 휴무

    @Column(name="fr_stat_saturday")
    private String frStatSaturday;

    @Column(name="fr_stat_holiday")
    private String frStatHoliday;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
