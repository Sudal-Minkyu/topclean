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
 * Remark : Toppos 지사 공지사항 게시판 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListDto {
    private Long hnId;
    private String hnType; // 본사글 : 01, 지사글 : 02
    private String subject; // 제목
    private String insert_id;
    private LocalDateTime insertDateTime;

    public String getInsertDateTime() {
        return insertDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
