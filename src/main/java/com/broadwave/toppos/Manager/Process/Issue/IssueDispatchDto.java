package com.broadwave.toppos.Manager.Process.Issue;

import com.broadwave.toppos.common.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author Minkyu
 * Date : 2022-03-14
 * Time :
 * Remark : Toppos 지사 출고증 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDispatchDto {

    private String tagNo; // 태그번호
    private String barCode; // 지사코드2자리 + 가맹점코드3자리 + yyyymmdd + 4자리일련번호 autonum
    private String receptType; // 세탁타입
    private String frCode; // 가맹점코드
    private String frName; // 가맹점명
    private String brName; // 지사명
    private String frTelNo; // 가맹점번호
    private String brTelNo; // 지사번호

    private String qrCode; // QR코드

    private String miDt; // 출고일자
    private Integer miDegree; // 출고차수

    private BigInteger t01Count; // 일반 갯수
    private BigInteger t02Count; // 반품 갯수
    private BigInteger t03Count; // 재세탁 갯수
    private BigInteger t04Count; // 가죽,특수세탁 갯수
    private BigInteger t05Count; // 털,부속품 갯수
    private BigInteger t06Count; // 운동화 갯수
    private BigInteger t07Count; // 침구,커튼 갯수
    private BigInteger t08Count; // 오염제거 갯수
    private BigInteger total; // 총 합계

    public StringBuffer getTagNo() {
        StringBuffer getTagNo = new StringBuffer(tagNo);
        getTagNo.insert(3,'-');
        getTagNo.insert(5,'-');
        return getTagNo;
    }

    public String getFrTelNo() {
        if(frTelNo != null) {
            return CommonUtils.hpNumberChange(frTelNo);
        }else{
            return null;
        }
    }

    public String getBrTelNo() {
        if(brTelNo != null) {
            return CommonUtils.hpNumberChange(brTelNo);
        }else{
            return null;
        }
    }

    public StringBuffer getMiDt() {
        if(!miDt.equals("")){
            StringBuffer getMiDt = new StringBuffer(miDt);
            getMiDt.insert(4,'-');
            getMiDt.insert(7,'-');
            return getMiDt;
        }else{
            return null;
        }
    }

}
