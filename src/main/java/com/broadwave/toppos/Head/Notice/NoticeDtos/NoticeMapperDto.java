package com.broadwave.toppos.Head.Notice.NoticeDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-04-05
 * Time :
 * Remark : Toppos 공지사항등록 MapperDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeMapperDto {

    private Long id; // 게시판ID
    private String subject; // 게시판 제목
    private String content; // 게시물 내용
    private List<MultipartFile> multipartFileList;
    private List<Long> deleteFileList; // 지울 파일들의 id 리스트

}
