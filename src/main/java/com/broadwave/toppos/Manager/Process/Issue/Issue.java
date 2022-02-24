package com.broadwave.toppos.Manager.Process.Issue;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-02-10
 * Time :
 * Remark : Toppos 지사 출고처리 마스터테이블
 */
@Entity
@Data
@Builder
@EqualsAndHashCode(of = "miId")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="mr_issue")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="mi_id")
    private Long miId;

    @Column(name="mi_no")
    private String miNo; // 지사코드2자리 + 가맹점코드3자리 + yyyymmdd + 4자리일련번호 autonum

    @Column(name="fr_code")
    private String frCode; // 가맹점코드 3자리

    @Column(name="br_code")
    private String brCode; // 지사코드 2자리

    @Column(name="mi_degree")
    private Integer miDegree; // 출고차수

    @Column(name="mi_dt")
    private String miDt; // 출고일자

    @Column(name="mi_time")
    private LocalDateTime miTime; // 출고일시간

    @Column(name="insert_id")
    private String insert_id;

}
