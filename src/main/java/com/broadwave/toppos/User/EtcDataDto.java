package com.broadwave.toppos.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String fdTag; // 태그번호
    private String frEstimateDate; // 출고예정일

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
