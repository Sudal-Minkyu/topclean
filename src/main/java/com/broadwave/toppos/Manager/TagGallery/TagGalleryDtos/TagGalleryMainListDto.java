package com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-03-24
 * Time :
 * Remark : Toppos NEW 택분실 TagGalleryMainListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagGalleryMainListDto {

    private Long btId;
    private String btBrandName; // 추정브랜드명
    private String btMaterial; // 소재
    private String btInputDt; // 예상 지사입고일

    public StringBuffer getBtInputDt() {
        if(!btInputDt.equals("")){
            StringBuffer getBtInputDate = new StringBuffer(btInputDt);
            getBtInputDate.insert(4,'-');
            getBtInputDate.insert(7,'-');
            return getBtInputDate;
        }else{
            return null;
        }
    }

}
