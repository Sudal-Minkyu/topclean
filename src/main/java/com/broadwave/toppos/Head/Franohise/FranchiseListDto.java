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
public class FranchiseListDto {

    private String frCode; // 가맹점코드 3자리
    private String frName; // 가맹점명
    private String frRefCode; // 가맹점관리코드
    private String frContractDt; // 계약일자
    private String brContractFromDt; // 계약기간 from
    private String frContractToDt; // 계약기간 to
    private String frContractState; // 진행중 : 01, 계약완료 : 02
    private String brAssignState; // 지사 배정상태 미배정: 01, 배정완료: 02
    private String frPriceGrade; // 가격등급 A,B,C,D,E
    private String frTagNo; // 가맹점 택번호 3자리
    private String frRemark; // 특이사항
    private String brName; // 배정지사명

    public String getFrRefCode() {
        return frRefCode;
    }

    public String getFrTagNo() {
        return frTagNo;
    }

    public String getFrPriceGrade() {
        return frPriceGrade;
    }

    public String getBrContractFromDt() {
        return brContractFromDt;
    }

    public String getBrAssignState() {
        return brAssignState;
    }
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
