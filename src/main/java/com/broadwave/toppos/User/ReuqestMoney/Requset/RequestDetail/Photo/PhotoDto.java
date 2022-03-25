package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-23
 * Time :
 * Remark : Toppos 사진촬영테이블 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDto {

    private String ffPath; // S3파일경로
    private String ffFilename; // S3파일명
    private String ffRemark; // 특이사항

}
