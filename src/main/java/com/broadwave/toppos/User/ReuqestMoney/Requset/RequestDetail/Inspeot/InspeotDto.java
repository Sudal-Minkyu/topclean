package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2022-01-14
 * Time :
 * Remark : Toppos 접수세부 검품정보 테이블 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspeotDto {

    private Long fiId;
    private String fiPhotoYn; // 검품 사진등록여부 (Y : 사진있음 , fs_photo_file 테이블에존재, N : 없음)
    private String fiSendMsgYn; // 메세지전송여부 Y , N  (기본값N)
    private String fiCustomerConfirm; // 고객 수락여부 ( 1: 미확인(기본값), 2:고객수락, 3:고객거부

    public String getFiSendMsgYn() {
        return fiSendMsgYn;
    }

    public String getFiCustomerConfirm() {
        return fiCustomerConfirm;
    }

    public Long getFiId() {
        return fiId;
    }

    public String getFiPhotoYn() {
        return fiPhotoYn;
    }
}
