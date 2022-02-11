package com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-02-03
 * Time :
 * Remark : Toppos 지사 택분실게시판 ViewDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagNoticeViewDto {
    private Long htId;
    private String brCode; // 지사코드 2자리
    private String subject; // 헌재 글 제목
    private String content; // 헌재 글 내용
    private String name; // 작성자
    private LocalDateTime insertDateTime;

    public String getInsertDateTime() {
        return insertDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }

}
