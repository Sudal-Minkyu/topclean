package com.broadwave.toppos.Manager.TagNotice.TagNoticeFile;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2022-02-21
 * Time :
 * Remark : Toppos 택분실 게시판 FileDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagNoticeFileDto {

    private String hfPath; // S3파일경로
    private String hfFilename; // S3파일명
    private String hfOriginalFilename; // 원래 파일 명

}
