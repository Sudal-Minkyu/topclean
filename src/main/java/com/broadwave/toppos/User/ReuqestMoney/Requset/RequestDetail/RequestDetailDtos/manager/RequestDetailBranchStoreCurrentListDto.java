package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Minkyu
 * Date : 2022-02-11
 * Time :
 * Remark : Toppos 지사 가맹점강제출고 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailBranchStoreCurrentListDto {

    private String frCode; // 가맹점코드
    private String frName; // 가맹점명
    private String fdS2Dt; // 입고일자

    private Integer input_cnt; // 입고건수
    private Integer output_cnt; // 출고건수
    private Integer remain_cnt; // 체류건수

    private Integer tot_amt; // 접수총액 - 입고건수 금액 sum

//    public String getInsertDt() {
//        return insertDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
//    }

}
