package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.MessageHistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Minkyu
 * Date : 2022-04-18
 * Time :
 * Remark : Toppos 가맹점 메세지 전송내역 오른쪽 리스트 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageHistorySubListDto {

    private String fmType; // 전송유형 검품: 01: 검품, 02: 영수증, 04: 수동만

    private Timestamp insertDateTime; // 전송일시
    private String bcName; // 고객명
    private String bcHp; // 수신번호
    private String fmMessage; // 메세지 내용

}
