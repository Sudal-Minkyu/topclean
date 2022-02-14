package com.broadwave.toppos.Manager.Process.IssueForce;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-02-11
 * Time :
 * Remark : Toppos 지사 강제 출고처리 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="mr_issue_force")
public class IssueForce {

    @Id
    private Long fdId; // 접수세부테이블 고유값 ID

    @Column(name="fr_code")
    private String frCode; // 가맹점코드

    @Column(name="br_code")
    private String brCode; // 지점코드

    @Column(name="mr_dt")
    private String mrDt; // 강제출고 날짜

    @Column(name="mr_time")
    private LocalDateTime mrTime; // 강제출고 시간

    @Column(name="insert_id")
    private String insert_id; // 시행한 아이디

}
