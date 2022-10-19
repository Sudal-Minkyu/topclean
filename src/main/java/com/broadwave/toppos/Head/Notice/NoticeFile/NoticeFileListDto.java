package com.broadwave.toppos.Head.Notice.NoticeFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-03-07
 * Time :
 * Remark : Toppos 공지사항 게시판 FileListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeFileListDto {

    private Long fileId; // ID
    private String filePath; // S3파일경로
    private String fileFileName; // S3파일명
    private String fileOriginalFilename; // 원래 파일 명
    private Long fileVolume; // 파일용량

}
