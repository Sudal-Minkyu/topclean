package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2022-01-17
 * Time :
 * Remark : Toppos 가맹점 접수 검품 등록여부 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspeotYnDto {

    private Long fdId; // 세부테이블 고유ID값

    public Long getFdId() {
        return fdId;
    }
}
