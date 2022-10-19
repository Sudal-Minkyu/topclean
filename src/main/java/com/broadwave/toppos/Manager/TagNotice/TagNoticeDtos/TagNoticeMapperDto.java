package com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-02-21
 * Time :
 * Remark : Toppos 가맹점 택분실 MapperDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagNoticeMapperDto {

    private Long id; // 게시판ID
    private String subject; // 게시판 제목
    private String content; // 게시물 내용
    private List<MultipartFile> multipartFileList;
    private List<Long> deleteFileList; // 지울 파일들의 id 리스트

}
