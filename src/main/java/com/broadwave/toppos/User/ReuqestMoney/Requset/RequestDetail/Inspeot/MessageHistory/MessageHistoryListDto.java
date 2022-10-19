package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.MessageHistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Minkyu
 * Date : 2022-04-18
 * Time :
 * Remark : Toppos 가맹점 메세지 전송내역 왼쪽 리스트 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageHistoryListDto {

    private String insertYyyymmdd;

    private BigInteger total; // 총건수

    private BigInteger fmType01_cnt; // 검품 건수 : fmTpye -> "01"
    private BigInteger fmType02_cnt; // 검품 건수 : fmTpye -> "02"
    private BigInteger fmType04_cnt; // 수동 건수 : fmTpye -> "04"
    private BigInteger fmTypeXX_cnt; // 완성품메시지 건수

    public StringBuffer getInsertYyyymmdd() {
        if(insertYyyymmdd != null && !insertYyyymmdd.equals("")){
            StringBuffer getInsertYyyymmdd = new StringBuffer(insertYyyymmdd);
            getInsertYyyymmdd.insert(4,'-');
            getInsertYyyymmdd.insert(7,'-');
            return getInsertYyyymmdd;
        }else{
            return null;
        }
    }

}
