package com.broadwave.toppos.Manager.Calendar;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark : Toppos 지사 업무휴무일지정 복합키 클래스
 */
@Data
class BranchCalendarPK implements Serializable {

    private String brCode;
    private String bcDate;

}
