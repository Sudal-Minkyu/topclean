package com.broadwave.toppos.Head.Calculate.DailySummary;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2022-06-14
 * Time :
 * Remark : Toppos 가맹점별 일정산요약 PK
 */
@Data
class DaliySummaryPK implements Serializable {

    private String hsYyyymmdd; // 일정산일자
    private String brCode; // 지사 코드 2자리
    private String frCode; // 가맹점 코드 3자리

}
