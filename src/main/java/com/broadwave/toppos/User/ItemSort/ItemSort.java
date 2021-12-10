package com.broadwave.toppos.User.ItemSort;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-08
 * Time :
 * Remark : Toppos 가맹점별 상품정렬 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(ItemSortPK.class)
@Table(name="bs_franchise_itemsort")
public class ItemSort {

    @Id
    @Column(name="fr_code")
    private String frCode; // 가맹점 코드 3자리

    @Id
    @Column(name="bi_itemcode")
    private String biItemcode; // 상품코드 7자리

    @Column(name="bi_item_mgroup")
    private String biItemMgroup; // 대 + 중분류코드

    @Column(name="bf_sort")
    private Integer bfSort; // 정렬순서

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
