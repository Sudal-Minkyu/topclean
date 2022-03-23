package com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-18
 * Time :
 * Remark : Toppos 가맹점 NEW 택분실 TagGalleryListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagGalleryListDto {

    private Timestamp insertDateTime; // 등록일시
    private BigInteger btId;
    private String btBrandName; // 추정브랜드명
    private String btInputDate; // 예상 지사입고일
    private String btMaterial; // 소재
    private String btRemark; // 특이사항
    private String frName; // 가맹점명
    private String brCloseYn; // 종료(게시물내림) 여부 , 기본값 N

    public StringBuffer getBtInputDate() {
        if(!btInputDate.equals("")){
            StringBuffer getBtInputDate = new StringBuffer(btInputDate);
            getBtInputDate.insert(4,'-');
            getBtInputDate.insert(7,'-');
            return getBtInputDate;
        }else{
            return null;
        }
    }

    public String getInsertDateTime() {
        return insertDateTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }

}
