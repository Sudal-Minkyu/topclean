package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-03-31
 * Time :
 * Remark : Toppos 접수세부 검품 정보호출 테이블 InfoDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspeotInfoDto {

    private Long fiId; // 검품 테이블 ID
    private Long bcId; // 고객 테이블 ID
    private String fiComment; // 검품 특이사항
    private Integer fiAddAmt; // 세탁 추가발생 비용
    private String fiPhotoYn; // 검품 사진등록여부 (Y : 사진있음 , fs_photo_file 테이블에존재, N : 없음)
    private String fiSendMsgYn; // 메세지전송여부 Y , N  (기본값N)
    private String fiCustomerConfirm; // 고객 수락여부 ( 1: 미확인(기본값), 2:고객수락, 3:고객거부

    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )

    private String frTelNo; // 가맹점 전화번호

//    private LocalDateTime insertDt; // 등록날짜

//    public String getInsertDt() {
//        return insertDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//    }

}
