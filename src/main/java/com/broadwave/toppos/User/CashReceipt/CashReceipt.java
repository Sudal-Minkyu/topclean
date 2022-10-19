package com.broadwave.toppos.User.CashReceipt;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-04-27
 * Time :
 * Remark : Toppos 가맹점 현금영수증발행 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "fcId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_request_cashreceipt")
public class CashReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fc_id")
    private Long fcId;

    @ManyToOne(targetEntity = Request.class,fetch = FetchType.LAZY)
    @JoinColumn(name="fr_id")
    private Request frId; // 접수마스터 테이블 ID값

    @ManyToOne(targetEntity = Customer.class,fetch = FetchType.LAZY)
    @JoinColumn(name="bc_id")
    private Customer bcId; // 고객 ID값

    @Column(name="fc_yyyymmdd")
    private String fcYyyymmdd; // 등록날짜

    @Column(name="fc_in_type")
    private String fcInType; // 입력타입 (01:결제, 02:미수,03:통합결제) -> 01 - 접수화면에서결제시, 02 - 미수처리화면에서결제시, 03 - 통합결제에서 처리시

    @Column(name="fc_type")
    private String fcType; // 현금영수증타입 (1:소비자소득공제, 2:사업자지출증빙, 3: 자진발급)

    @Column(name="fc_real_amt")
    private Integer fcRealAmt; // 발행금액

    @Column(name="fc_cancel_yn")
    private String fcCancelYn; // 발행취소 여부 : 기본값 N

    @Column(name="fc_cat_approvalno")
    private String fcCatApprovalno; // 승인번호 ex 73536757

    @Column(name="fc_cat_approvaltime")
    private String fcCatApprovaltime; // 승인시간 ex 2111241411144

    @Column(name="fc_cat_cardno")
    private String fcCatCardno; // 카드번호 ex 010-****-7785

    @Column(name="fc_cat_issuercode")
    private String fcCatIssuercode; // 이슈코드 ex > 01

    @Column(name="fc_cat_issuername")
    private String fcCatIssuername; // 이슈명칭

    @Column(name="fc_cat_muechantnumber")
    private String fcCatMuechantnumber; // 카드가맹점코드 ex > 72729972

    @Column(name="fc_cat_message1")
    private String fcCatMessage1; // 단말기메세제1 : ex> IBK 비씨카드

    @Column(name="fc_cat_message2")
    private String fcCatMessage2; // 단말기메세제2 : ex> OK:73536757

    @Column(name="fc_cat_notice1")
    private String fcCatNotice1; // 단말기Notice1 : ex> EDC매출표

    @Column(name="fc_cat_totamount")
    private String fcCatTotamount; // 결제금액 : ex> 000012000

    @Column(name="fc_cat_vatamount")
    private String fcCatVatamount; // 부가세금액 : ex> 000001090

    @Column(name="fc_cat_telegramflagt")
    private String fcCatTelegramflagt; // 전문구분 : ex a1

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insert_date;

}
