package com.broadwave.toppos.Manager.TagNotice.TagNoticeFile;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2022-02-22
 * Time :
 * Remark : Toppos 택분실 게시판 FileListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagNoticeFileListDto {

    private Long fileId; // ID
    private String filePath; // S3파일경로
    private String fileFileName; // S3파일명
    private String fileOriginalFilename; // 원래 파일 명
    private Long fileVolume; // 파일용량

}
