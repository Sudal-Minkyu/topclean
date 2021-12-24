package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-23
 * Time :
 * Remark : Toppos 사진촬영테이블 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDto {

    private String ffPath; // S3파일경로
    private String ffFilename; // S3파일명
    private String ffRemark; // 특이사항

    public String getFfPath() {
        return ffPath;
    }

    public String getFfFilename() {
        return ffFilename;
    }

    public String getFfRemark() {
        return ffRemark;
    }
}
