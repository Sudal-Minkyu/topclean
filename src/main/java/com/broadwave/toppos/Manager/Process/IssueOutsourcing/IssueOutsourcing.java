package com.broadwave.toppos.Manager.Process.IssueOutsourcing;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-05-20
 * Time :
 * Remark : Toppos 지사 외주 출고처리 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="mr_issue_outsourcing")
public class IssueOutsourcing {

    @Id
    private Long fdId; // 접수세부테이블 고유값 ID

    @Column(name="br_code")
    private String brCode; // 지점코드

    @Column(name="mo_dt")
    private String moDt; // 외주출고 날짜

    @Column(name="mo_time")
    private LocalDateTime moTime; // 외주출고 시간

    @Column(name="insert_id")
    private String insert_id; // 시행한 아이디

}
