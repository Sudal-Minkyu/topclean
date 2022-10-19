package com.broadwave.toppos.Manager.Calendar;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-18
 * Time :
 * Remark : Toppos 지사 업무휴무일지정 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(BranchCalendarPK.class)
@Table(name="bs_branch_calendar")
public class BranchCalendar {

    @Id
    @Column(name = "br_code")
    private String brCode; // 지사코드

    @Id
    @Column(name = "bc_date")
    private String bcDate; // 날짜 : yyyymmdd

    @Column(name="bc_dayoff_yn")
    private String bcDayoffYn; // 휴무상태 : Y/N

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
