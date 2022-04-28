package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-01-04
 * Time :
 * Remark : Toppos 접수세부 검품정보 테이블 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspeotListDto {

    private Long fiId; // 검품 테이블 ID
    private String fiType; // 검품 타입 , F : 가맹검품, B:지사검품(확인품)
    private String fiComment; // 검품 특이사항
    private Integer fiAddAmt; // 세탁 추가발생 비용
    private String fiPhotoYn; // 검품 사진등록여부 (Y : 사진있음 , fs_photo_file 테이블에존재, N : 없음)
    private String fiSendMsgYn; // 메세지전송여부 Y , N  (기본값N)
    private String fiCustomerConfirm; // 고객 수락여부 ( 1: 미확인(기본값), 2:고객수락, 3:고객거부
    private LocalDateTime insertDt; // 등록날짜
//    private String ffPath; // S3 파일경로
//    private String ffFilename; // S3 파일명

    public String getInsertDt() {
        return insertDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
