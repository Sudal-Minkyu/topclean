package com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose.QrCloseDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-07-12
 * Time :
 * Remark : Toppos 가맹점 QrClost 리스트 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCloseCountSubListDto {

    private LocalDateTime insertDt; // 등록시간
    private String frName; // 가맹점명

    public String getInsertDt() {
        return insertDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }

}
