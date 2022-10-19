package com.broadwave.toppos.Head.Notice.NoticeDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-02-07
 * Time :
 * Remark : Toppos 공지사항 게시판 ViewDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeViewDto {
    private Long hnId;
    private String subject; // 헌재 글 제목
    private String content; // 헌재 글 내용
    private String hnType; // 본사글 : 01, 지사글 : 02
    private String name; // 작성자

    private LocalDateTime insertDateTime;

    public String getInsertDateTime() {
        return insertDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }

}
