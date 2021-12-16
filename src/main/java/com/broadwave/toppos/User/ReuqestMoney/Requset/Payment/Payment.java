package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-15
 * Time :
 * Remark : Toppos 가맹점 접수 결제테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_request_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fp_id")
    private Long id; // 고유값 ID

    @ManyToOne(targetEntity = Request.class,fetch = FetchType.LAZY)
    @JoinColumn(name="fr_id")
    private Request fpId; // 접수 마스터테이블 ID값

    @ManyToOne(targetEntity = Customer.class,fetch = FetchType.LAZY)
    @JoinColumn(name="bc_id")
    private Customer bcId; // 고객 ID값

    @Column(name="fp_type")
    private String fpType; // 결제타입 (01:현금, 02:카드,03:적립금)

    @Column(name="fp_month")
    private Integer fpMonth; // 카드할부 ( 0: 일시불, 2: 2개월 ~ 12 : 12개월) - 기본값 0

    @Column(name="fp_amt")
    private Integer fpAmt; // 결제금액( 미수에서 카드결제시 카드결제금액보다작을수있다.)

    @Column(name="fp_real_amt")
    private Integer fpRealAmt; // 실제결제금액(총결제금액 -> 여러건을 미수관리에서 합쳐서 결제할경우 총결제금액)

    @Column(name="fp_collect_amt")
    private Integer fpCollectAmt; // 미수 완납금액

    @Column(name="fp_cancel_yn")
    private String fpCancelYn; // 결제취소 여부 : 기본값 N

    @Column(name="fp_saved_money_yn")
    private String fpSavedMoneyYn; // 결제취소후 적립금전환 여부 : 기본값 N

    @Column(name="fp_cat_approvalno")
    private String fpCatApprovalno; // 카드승인번호 ex 73536757

    @Column(name="fp_cat_approvaltime")
    private String fpCatApprovaltime; // 카드승인시간 ex 2111241411144

    @Column(name="fp_cat_cardno")
    private String fpCatCardno; // 카드번호 ex 942003******8000

    @Column(name="fp_cat_issuercode")
    private String fpCatIssuercode; // 이슈코드 ex > 01

    @Column(name="fp_cat_issuername")
    private String fpCatIssuername; // 이슈명칭 ex > IBK 비씨카드

    @Column(name="fp_cat_muechantnumber")
    private String fpCatMuechantnumber; // 카드가맹점코드 ex > 72729972

    @Column(name="fp_cat_message1")
    private String fpCatMessage1; // 단말기메세제1 : ex> IBK 비씨카드

    @Column(name="fp_cat_message2")
    private String fpCatMessage2; // 단말기메세제2 : ex> IBK 비씨카드

    @Column(name="fp_cat_notice1")
    private String fpCatNotice1; // 단말기Notice1 : ex> EDC매출표

    @Column(name="fp_cat_totamount")
    private String fpCatTotamount; // 결제금액 : ex> 000012000

    @Column(name="fp_cat_vatamount")
    private String fpCatVatamount; // 부가세금액 : ex> 000001090

    @Column(name="fp_cat_telegramflagt")
    private String fpCatTelegramflagt; // 전문구분 : ex a1

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insert_date;

}
