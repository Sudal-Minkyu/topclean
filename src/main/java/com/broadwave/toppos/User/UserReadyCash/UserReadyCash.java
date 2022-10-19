package com.broadwave.toppos.User.UserReadyCash;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-06-07
 * Time :
 * Remark : Toppos 가맹점별 일자별 현금 준비금
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(UserReadyCashPK.class)
@Table(name="bs_franchise_ready_cash")
public class UserReadyCash {

    @Id
    @Column(name="fr_id")
    private Long frId; // 가맹점 ID

    @Id
    @Column(name="bc_yyyymmdd")
    private String bcYyyymmdd; // 코드

    @Id
    @Column(name="bc_ready_amt")
    private Integer bcReadyAmt; // 준비금

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
