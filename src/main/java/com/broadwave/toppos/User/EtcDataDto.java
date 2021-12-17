package com.broadwave.toppos.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark : 가맹점 접수페이지 Etc데이터
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtcDataDto {

    private String frCode; // 가맹점코드 3자리
    private String frName; // 가맹점명
    private String fdTag; // 태그번호
    private String frEstimateDate; // 출고예정일
    private String frBusinessNo; // 사업자번호(10자리)
    private String frRpreName; // 가맹점주이름
    private String frTelNo; // 가맹점 전화번호

    public String getFrBusinessNo() {
        return frBusinessNo;
    }

    public String getFrRpreName() {
        return frRpreName;
    }

    public String getFrTelNo() {
        return frTelNo;
    }

    public String getFrCode() {
        return frCode;
    }

    public String getFrName() {
        return frName;
    }

    public String getFdTag() {
        return fdTag;
    }

    public StringBuffer getFrEstimateDate() {
        StringBuffer getFrEstimateDate = new StringBuffer(frEstimateDate);
        getFrEstimateDate.insert(4,'-');
        getFrEstimateDate.insert(7,'-');
        return getFrEstimateDate;
    }
}
