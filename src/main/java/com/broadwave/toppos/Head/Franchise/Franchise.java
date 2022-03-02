package com.broadwave.toppos.Head.Franchise;

import com.broadwave.toppos.Head.Branoh.Branch;
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
    private String frTagNo; // 가맹점 택번호 3자리

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

    @ManyToOne(targetEntity = Branch.class,fetch = FetchType.EAGER)
    @JoinColumn(name="br_id")
    private Branch brId; // 배정된 지사 ID

    @Column(name="br_code")
    private String brCode; // 배정된 지사코드

    @Column(name="br_assign_state")
    private String brAssignState; // 지사 배정상태 미배정: 01, 배정완료: 02

    @Column(name="fr_last_tagno")
    private String frLastTagno; // 가맹점 태그번호

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
