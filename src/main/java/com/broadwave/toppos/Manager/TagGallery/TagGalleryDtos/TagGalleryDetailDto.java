package com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-03-18
 * Time :
 * Remark : Toppos 가맹점 NEW 택분실 TagGalleryDetailDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagGalleryDetailDto {

    private String btBrandName; // 추정브랜드명
    private String btInputDate; // 예상 지사입고일
    private String btMaterial; // 소재
    private String btRemark; // 특이사항

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

}
