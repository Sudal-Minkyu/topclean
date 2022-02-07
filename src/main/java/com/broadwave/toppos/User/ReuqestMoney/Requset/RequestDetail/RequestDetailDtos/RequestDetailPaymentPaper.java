package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-02-07
 * Time :
 * Remark : Toppos 가맹점 영수증출력 Item ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailPaymentPaper {

    private String fdTag; // 택번호
    private String fdColor; // 색상코드 (00:미선택 01 흰색 02:검정 03: 회색, 04 빨강 05:주황, 06: 노랑, 07 초록 08 파랑 09:남색 10 보라 11 핑크)
//        private String itemname; // 중분류명+" "+상품명+" "+대분류명
    private String bgName; // 대분류명
    private String bsName; // 중분류명
    private String biName; // 상품명

    private String fdSpecialYn;
    private Integer fdTotAmt;
    private String fdEstimateDt;

    public String getItemName() {
        if(bsName.equals("일반")){
            return biName+" "+bgName;
        }else{
            return biName+" "+bsName+" "+bgName;
        }
    }

    public StringBuffer getFdEstimateDt() {
        if(!fdEstimateDt.equals("")){
            StringBuffer getFdEstimateDt = new StringBuffer(fdEstimateDt);
            getFdEstimateDt.insert(4,"-");
            getFdEstimateDt.insert(7,"-");
            return getFdEstimateDt;
        }else{
            return null;
        }
    }

}