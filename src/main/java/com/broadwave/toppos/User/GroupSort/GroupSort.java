package com.broadwave.toppos.User.GroupSort;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-08
 * Time :
 * Remark : Toppos 가맹점별 대분류 정렬 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(GroupSortPK.class)
@Table(name="bs_franchise_groupsort")
public class GroupSort {

    @Id
    @Column(name="fr_code")
    private String frCode; // 가맹점 코드 3자리

    @Id
    @Column(name="bg_item_groupcode")
    private String bgItemGroupcode; // 대분류 코드

    @Column(name="bg_sort")
    private Integer bgSort; // 정렬순서

    @Column(name="bg_favorite_yn")
    private String bgFavoriteYn; // 즐겨찾기 YN , 기본값 N

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
