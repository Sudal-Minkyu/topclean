package com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    private LocalDateTime insertDateTime; // 등록일시
    private Long btId;
    private String btBrandName; // 추정브랜드명
    private String btInputDate; // 예상 지사입고일
    private String btMaterial; // 소재
    private String btRemark; // 특이사항
//    private String bfPath1; // 사진PATH_1
//    private String bfFilename1; // 사진Filename_1
//    private String bfPath2; // 사진URL_2
//    private String bfFilename2; // 사진Filename_2
//    private String bfPath3; // 사진URL_3
//    private String bfFilename3; // 사진Filename_3
//    private String frCheck; // 가맹 응답상태

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
        return insertDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }

//    public String getBfPath1() {
//        return bfPath1+"s_"+bfFilename1;
//    }
//
//    public String getBfPath2() {
//        return bfPath2+"s_"+bfFilename2;
//    }
//
//    public String getBfPath3() {
//        return bfPath3+"s_"+bfFilename3;
//    }

//    public String getFrCheck() {
//        return frCheck;
//    }
}
