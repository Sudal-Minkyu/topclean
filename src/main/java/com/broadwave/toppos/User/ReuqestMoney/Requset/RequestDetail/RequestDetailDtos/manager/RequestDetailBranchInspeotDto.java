package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-03-17
 * Time :
 * Remark : Toppos 지사 확인품등록시 필요한 데이터호출용 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailBranchInspeotDto {

    private RequestDetail requestDetail;
    private String brName; // 지사명
    private String frTelNo; // 가맹점번호
    private String bgName; // 대분류명
    private String biName; // 상품명

}
