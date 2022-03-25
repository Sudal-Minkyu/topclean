package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.force;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-01-24
 * Time :
 * Remark : Toppos 가맹점 강제 입고처리 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_inhouse_force")
public class InhouceForce {

    @Id
    private Long fdId; // 접수세부테이블 고유값 ID

    @Column(name="fr_code")
    private String frCode; // 가맹점코드

    @Column(name="br_code")
    private String brCode; // 지점코드

    @Column(name="fi_dt")
    private String fiDt; // 강제입고 날짜

    @Column(name="fi_time")
    private LocalDateTime fiTime; // 강제입고 시간

    @Column(name="insert_id")
    private String insert_id; // 시행한 아이디

}
