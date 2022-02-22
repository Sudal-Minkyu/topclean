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
 * Remark : Toppos 가맹점 접수세부 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagNoticeMapperDto {

    private String htSubject; // 게시판 제목
    private String htContent; // 게시물 내용
    private List<MultipartFile> multipartFileList;

}
