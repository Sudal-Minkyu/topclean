package com.broadwave.toppos.Head.Calculate.ReceiptDaily;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-06-15
 * Time :
 * Remark : Toppos 본사 일정산 입금등록
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="hc_receipt_daily")
public class ReceiptDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hr_id")
    private Long hrId; // 일정산일자

    @Column(name="hs_yyyymmdd")
    private String hsYyyymmdd; // 일정산일자 (hc_monthly_summary)

    @Column(name="br_code")
    private String brCode; // 지사 코드 2자리

    @Column(name="fr_code")
    private String frCode; // 가맹점 코드 3자리

    @Column(name="hr_receipt_yyyymmdd")
    private String hrReceiptYyyymmdd; // 입금등록일자

    @Column(name="hr_receipt_sale_amt")
    private Integer hrReceiptSaleAmt; // 입금액(가맹점의 지사 입금액)

    @Column(name="hr_receipt_royalty_amt")
    private Integer hrReceiptRoyaltyAmt; // 입금액(가맹점 본사 로열티)

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
