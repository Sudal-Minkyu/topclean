package com.broadwave.toppos.Head.Branoh.BranchDtos;

import lombok.*;

import javax.persistence.Column;

/**
 * @author Minkyu
 * Date : 2021-11-09
 * Time :
 * Remark : Toppos 지사 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchListDto {

    private String brCode; // 지사코드
    private String brName; // 지사명
    private String brTelNo; // 지사 전화번호
    private String brContractDt; // 계약일자
    private String brContractFromDt; // 계약기간 from
    private String brContractToDt; // 계약기간 to
    private String brContractState; // 진행중 : 01, 계약완료 : 02

    private Double brCarculateRateBr; // 정산비율(지사)
    private Double brCarculateRateFr; // 정산비율(가맹점)
    private Double brRoyaltyRateBr; // 로얄티율(지사)
    private Double brRoyaltyRateFr; // 정산비율(가맹점)

    private String brRemark; // 특이사항

    public StringBuffer getBrContractDt() {
        if(!brContractDt.equals("")){
            StringBuffer getBrContractDt = new StringBuffer(brContractDt);
            getBrContractDt.insert(4,'-');
            getBrContractDt.insert(7,'-');
            return getBrContractDt;
        }else{
            return null;
        }
    }

    public StringBuffer getBrContractFromDt() {
        if(!brContractFromDt.equals("")){
            StringBuffer getBrContractFromDt = new StringBuffer(brContractFromDt);
            getBrContractFromDt.insert(4,'-');
            getBrContractFromDt.insert(7,'-');
            return getBrContractFromDt;
        }else{
            return null;
        }
    }

    public StringBuffer getBrContractToDt() {
        if(!brContractToDt.equals("")){
            StringBuffer getBrContractToDt = new StringBuffer(brContractToDt);
            getBrContractToDt.insert(4,'-');
            getBrContractToDt.insert(7,'-');
            return getBrContractToDt;
        }else{
            return null;
        }
    }

}
