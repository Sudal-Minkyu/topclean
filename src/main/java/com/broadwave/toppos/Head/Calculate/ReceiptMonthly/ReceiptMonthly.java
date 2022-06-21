package com.broadwave.toppos.Head.Calculate.ReceiptMonthly;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-06-15
 * Time :
 * Remark : Toppos 본사 월정산 입금등록
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="hc_receipt_monthly")
public class ReceiptMonthly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hr_id")
    private Long hrId; // 월정산일자

    @Column(name="hs_yyyymm")
    private String hsYyyymm; // 월정산일자 (hc_monthly_summary)

    @Column(name="br_code")
    private String brCode; // 지사 코드 2자리

    @Column(name="hr_receipt_yyyymmdd")
    private String hrReceiptYyyymmdd; // 입금등록일자

    @Column(name="hr_receipt_br_royalty_amt")
    private Integer hrReceiptBrRoyaltyAmt; // 입금액(지사로열티)

    @Column(name="hr_receipt_fr_royalty_amt")
    private Integer hrReceiptFrRoyaltyAmt; // 입금액(가맹점 본사 로열티)

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
