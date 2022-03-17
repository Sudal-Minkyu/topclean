package com.broadwave.toppos.Head.Branoh;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark : Toppos 지사 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_branch")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="br_id")
    private Long id;

    @Column(unique = true,name="br_code")
    private String brCode; // 지점코드 2자리

    @Column(name="br_name")
    private String brName; // 지점명

    @Column(name="br_tel_no")
    private String brTelNo; // 지사 전화번호

    @Column(name="br_contract_dt")
    private String brContractDt; // 계약일자

    @Column(name="br_contract_from_dt")
    private String brContractFromDt; // 계약기간 from

    @Column(name="br_contract_to_dt")
    private String brContractToDt; // 계약기간 to

    @Column(name="br_contract_state")
    private String brContractState; // 진행중 : 01, 계약완료 : 02

//    @Column(name="br_carculate_rate_hq")
//    private Double brCarculateRateHq; // 정산비율(본사)

    @Column(name="carculate_rate_br")
    private Double brCaculateRateBr; // 정산비율(지사)

    @Column(name="carculate_rate_fr")
    private Double brCaculateRateFr; // 정산비율(가맹점)

    @Column(name="royalty_rate_br")
    private Double brRoyaltyRateBr; // 로얄티율(지사)

    @Column(name="royalty_rate_fr")
    private Double brRoyaltyRateFr; // 정산비율(가맹점)

    @Column(name="br_remark")
    private String brRemark; // 특이사항

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
