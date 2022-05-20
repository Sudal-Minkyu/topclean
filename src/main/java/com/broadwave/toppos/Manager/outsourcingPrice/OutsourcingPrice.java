package com.broadwave.toppos.Manager.outsourcingPrice;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author DongA
 * Date : 2022-05-13
 * Time :
 * Remark : 지사 외주가격 도메인 설정
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@IdClass(OutsourcingPricePK.class)
@Table(name = "br_item_outsourcing_price")
public class OutsourcingPrice {

    @Id
    @Column(name = "bi_itemcode")
    private String biItemcode; // 상품코드 7자리

    @Id
    @Column(name = "br_code")
    private String brCode; // 지사코드 2자리

    @Column(name = "bp_outsourcing_yn")
    private String bpOutsourcingYn; // 외주 대상품목 (Y/N)

    @Column(name = "bp_outsourcing_price")
    private Integer bpOutsourcingPrice; // 외주 가격

    @Column(name = "bp_remark")
    private String bpRemark; // 특이사항

    @Column(name = "modify_id")
    private String modify_id;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @Column(name = "insert_id")
    private String insert_id;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;
}
