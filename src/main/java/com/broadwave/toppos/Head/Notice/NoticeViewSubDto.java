package com.broadwave.toppos.Head.Notice;

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
 * Remark : Toppos 공지사항 게시판 이전글,다음글 ViewSubDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeViewSubDto {

    private Long subId;
    private String subSubject;
    private LocalDateTime subInsertDateTime;

    public String getSubInsertDateTime() {
        return subInsertDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }

}
