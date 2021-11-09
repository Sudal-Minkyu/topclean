package com.broadwave.toppos.Head.Franohise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark : Toppos 가맹점 MapperDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseMapperDto {

    private String frCode; // 가맹점코드 3자리
    private String frName; // 가맹점명
    private String frContractDt; // 계약일자
    private String frContractFromDt; // 계약기간 from
    private String frContractToDt; // 계약기간 to
    private String frContractState; // 진행중 : 01, 계약완료 : 02
    private String frRemark; // 특이사항

    public String getFrCode() {
        return frCode;
    }

    public String getFrName() {
        return frName;
    }

    public String getFrContractDt() {
        String getFrContractDt = frContractDt;
        getFrContractDt = getFrContractDt.replaceAll("-","");
        return getFrContractDt;
    }

    public String getFrContractFromDt() {
        String getFrContractFromDt = frContractFromDt;
        getFrContractFromDt = getFrContractFromDt.replaceAll("-","");
        return getFrContractFromDt;
    }

    public String getFrContractToDt() {
        String getFrContractToDt = frContractToDt;
        getFrContractToDt = getFrContractToDt.replaceAll("-","");
        return getFrContractToDt;
    }

    public String getFrContractState() {
        return frContractState;
    }

    public String getFrRemark() {
        return frRemark;
    }

    //    private String brAssignState; // 지사 배정상태 미배정: 01, 배정완료: 02

}
