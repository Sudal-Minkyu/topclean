package com.broadwave.toppos.User.ReuqestMoney.Requset;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.Payment;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-07
 * Time :
 * Remark : Toppos 가맹점 접수마스트 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fr_id")
    private Long id;

    @Column(unique = true,name="fr_no")
    private String frNo; // 접수코드

    @ManyToOne(targetEntity = Customer.class,fetch = FetchType.EAGER)
    @JoinColumn(name="bc_id")
    private Customer bcId; // 고객 ID값

    @Column(name="fr_code")
    private String frCode; // 가맹점코드

    @Column(name="br_code")
    private String bcCode; // 지점코드

    @Column(name="fr_yyyymmdd")
    private String frYyyymmdd; // 접수일자

    @Column(name="fr_qty")
    private Integer frQty; // 접수건수

    @Column(name="fr_normal_amount")
    private Integer frNormalAmount; // 접수금액

    @Column(name="fr_discount_amount")
    private Integer frDiscountAmount; // 할인금액

    @Column(name="fr_total_amount")
    private Integer frTotalAmount; // 합계금액

    @Column(name="fr_pay_amount")
    private Integer frPayAmount; // 결제금액

    @ManyToOne(targetEntity = Payment.class,fetch = FetchType.EAGER)
    @JoinColumn(name="fp_id")
    private Payment fpId; // 접수결제테이블 ID -> 미수완납시 처리에만 업데이트됨.

    @Column(name="fr_uncollect_yn")
    private String frUncollectYn; // 미수여부( 합계금액 > 결제금액 이면 Y, else N)

    @Column(name="fr_confirm_yn")
    private String frConfirmYn; // 임시저장여부플래그 Y만 정식데이터

    @Column(name="fr_ref_type")
    private String frRefType; // 접수타입(01:일반, 02:무인보관함, 03:배송APP)

    @Column(name="fr_ref_box_code")
    private String frRefBoxCode; // 무인보관함 연계시 무인보관함 접수번호

    @Column(name="fr_insert_id")
    private String fr_insert_id;

    @Column(name="fr_insert_date")
    private LocalDateTime fr_insert_date;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modify_date;

}
