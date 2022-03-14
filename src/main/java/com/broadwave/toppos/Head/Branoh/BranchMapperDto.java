package com.broadwave.toppos.Head.Branoh;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark : Toppos 지사 MapperDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchMapperDto {

    private String brCode; // 지점코드 2자리
    private String brName; // 지점명
    private String brTelNo; // 지사전화번호
    private String brContractDt; // 계약일자
    private String brContractFromDt; // 계약기간 from
    private String brContractToDt; // 계약기간 to
    private String brContractState; // 진행중 : 01, 계약완료 : 02
    private Double brCarculateRateHq; // 정산비율(본사)
    private Double brCarculateRateBr; // 정산비율(지사)
    private Double brCarculateRateFr; // 정산비율(가맹점)
    private String brRemark; // 특이사항

    public String getBrContractDt() {
        String getBrContractDt = brContractDt;
        getBrContractDt = getBrContractDt.replaceAll("-","");
        return getBrContractDt;
    }

    public String getBrContractFromDt() {
        String getBrContractFromDt = brContractFromDt;
        getBrContractFromDt = getBrContractFromDt.replaceAll("-","");
        return getBrContractFromDt;
    }

    public String getBrContractToDt() {
        String getBrContractToDt = brContractToDt;
        getBrContractToDt = getBrContractToDt.replaceAll("-","");
        return getBrContractToDt;
    }

}
