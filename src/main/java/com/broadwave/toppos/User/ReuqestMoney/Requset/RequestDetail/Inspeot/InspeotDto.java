package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-01-04
 * Time :
 * Remark : Toppos 접수세부 검품정보 테이블 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspeotDto {

//    private Long Id;
//    private RequestDetail fdId; // 접수 세부테이블 ID
    private String frCode; // 가맹점 코드 3자리
    private String brCode; // 지점 코드 2자리
    private String fiType; // 검품 타입 , F : 가맹검품, B:지사검품(확인품)
    private String fiComment; // 검품 특이사항
    private Integer fiAddAmt; // 세탁 추가발생 비용
    private String fiPhotoYn; // 검품 사진등록여부 (Y : 사진있음 , fs_photo_file 테이블에존재, N : 없음)
    private String fiSendMsgYn; // 메세지전송여부 Y , N  (기본값N)
    private String fiCustomerConfirm; // 고객 수락여부 ( 1: 미확인(기본값), 2:고객수락, 3:고객거부
    private LocalDateTime fiProgressStateDt; // 고객진행여부 변경상태
    private String fiMessage; // 고객 전송메세지
    private LocalDateTime fiMessageSendDt; // 메세지 송신시간

    public String getFrCode() {
        return frCode;
    }

    public String getBrCode() {
        return brCode;
    }

    public String getFiType() {
        return fiType;
    }

    public String getFiComment() {
        return fiComment;
    }

    public Integer getFiAddAmt() {
        return fiAddAmt;
    }

    public String getFiPhotoYn() {
        return fiPhotoYn;
    }

    public String getFiSendMsgYn() {
        return fiSendMsgYn;
    }

    public String getFiCustomerConfirm() {
        return fiCustomerConfirm;
    }

    public LocalDateTime getFiProgressStateDt() {
        return fiProgressStateDt;
    }

    public String getFiMessage() {
        return fiMessage;
    }

    public LocalDateTime getFiMessageSendDt() {
        return fiMessageSendDt;
    }
}
