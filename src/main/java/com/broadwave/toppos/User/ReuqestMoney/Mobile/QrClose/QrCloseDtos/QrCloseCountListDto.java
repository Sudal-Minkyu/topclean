package com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose.QrCloseDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-07-12
 * Time :
 * Remark : Toppos 가맹점 QrClost 건수 리스트 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCloseCountListDto {

    private String insertYyyymmdd; // 등록일자
    private Long fqCloseCnt; // 스캔건수(sum)

}
