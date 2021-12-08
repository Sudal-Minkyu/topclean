package com.broadwave.toppos.Head.Item.Price.FranchisePrice;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-30
 * Time :
 * Remark : Toppos 가맹점 특정상품관리  테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(FranchisePricePK.class)
@Table(name="bs_item_franchise_price")
public class FranchisePrice {

    @Id
    @Column(name="bi_itemcode")
    private String biItemcode; // 상품코드

    @Id
    @Column(name="fr_code")
    private String frCode; // 가맹점코드 3자리

    @Column(name="bf_price")
    private Integer bfPrice; // 가맹점 적용가격

    @Column(name="bf_remark")
    private String bfRemark; // 특이사항

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
