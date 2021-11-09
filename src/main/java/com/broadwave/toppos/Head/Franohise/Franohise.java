package com.broadwave.toppos.Head.Franohise;

import com.broadwave.toppos.Head.Branoh.Branoh;
import lombok.*;

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
@Table(name="bs_franohise")
public class Franohise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fr_id")
    private Long id;

    @Column(unique = true,name="fr_code")
    private String frCode; // 가맹점코드 3자리

    @Column(name="fr_name")
    private String frName; // 가맹점명

    @Column(name="fr_contract_dt")
    private String frContractDt; // 계약일자

    @Column(name="fr_contract_from_dt")
    private String frContractFromDt; // 계약기간 from

    @Column(name="fr_contract_to_dt")
    private String frContractToDt; // 계약기간 to

    @Column(name="fr_contract_state")
    private String frContractState; // 진행중 : 01, 계약완료 : 02

    @Column(name="fr_remark")
    private String frRemark; // 특이사항

    @ManyToOne(targetEntity = Branoh.class,fetch = FetchType.LAZY)
    @JoinColumn(name="br_id")
    private Branoh brId; // 배정된 지사 ID

    @Column(name="br_code")
    private String BrCode; // 배정된 지사코드

    @Column(name="br_assign_state")
    private String brAssignState; // 지사 배정상태 미배정: 01, 배정완료: 02

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
