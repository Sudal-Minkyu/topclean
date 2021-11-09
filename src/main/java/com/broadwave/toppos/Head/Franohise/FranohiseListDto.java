package com.broadwave.toppos.Head.Franohise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-11-09
 * Time :
 * Remark : Toppos 가맹점 ListDto
 */

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranohiseListDto {

    private String frCode; // 가맹점코드 3자리
    private String frName; // 가맹점명
    private String frContractDt; // 계약일자
    private String brContractFromDt; // 계약기간 from
    private String frContractToDt; // 계약기간 to
    private String frContractState; // 진행중 : 01, 계약완료 : 02
    private String frRemark; // 특이사항

    private String brName; // 배정지사명

    public String getFrCode() {
        return frCode;
    }

    public StringBuffer getFrContractDt() {
        StringBuffer getFrContractDt = new StringBuffer(frContractDt);
        getFrContractDt.insert(4,'-');
        getFrContractDt.insert(7,'-');
        return getFrContractDt;
    }

    public String getFrContractState() {
        return frContractState;
    }

    public String getFrRemark() {
        return frRemark;
    }

    public String getFrName() {
        return frName;
    }

    public StringBuffer getFrContractFromDt() {
        StringBuffer getFrContractFromDt = new StringBuffer(brContractFromDt);
        getFrContractFromDt.insert(4,'-');
        getFrContractFromDt.insert(7,'-');
        return getFrContractFromDt;
    }

    public StringBuffer getFrContractToDt() {
        StringBuffer getFrContractToDtDate = new StringBuffer(frContractToDt);
        getFrContractToDtDate.insert(4,'-');
        getFrContractToDtDate.insert(7,'-');
        return getFrContractToDtDate;
    }

    public String getBrName() {
        return brName;
    }

}
