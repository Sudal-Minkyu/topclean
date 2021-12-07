package com.broadwave.toppos.User.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-03
 * Time :
 * Remark : Toppos 고객테이블 InfoDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoDto {

    private Long bcId; // 고객고유 ID값
    private String bcName; // 고객명
    private String bcHp; // 휴대폰번호( "-" 빼고저장)
    private String bcAddress; // 주소
    private String bcGrade; // 고객등급( 일반-기본값 : 01, vip: 02, vvip: 03)
    private String bcValuation; // 고객평가( 별1개: 1, 별2개: 2, 별3개: 3, 별4개: 4, 별5개: 5)
    private String bcRemark; // 특이사항
    private String bcLastRequsetDt; // 마지막방문일자

    public String getBcRemark() {
        return bcRemark;
    }

    public String getBcLastRequsetDt() {
        return bcLastRequsetDt;
    }

    public Long getBcId() {
        return bcId;
    }

    public String getBcName() {
        return bcName;
    }

    public String getBcHp() {
        return bcHp;
    }

    public String getBcAddress() {
        return bcAddress;
    }

    public String getBcGrade() {
        return bcGrade;
    }

    public String getBcValuation() {
        return bcValuation;
    }
}
