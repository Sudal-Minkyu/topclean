package com.broadwave.toppos.Manager.TagNotice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-01-28
 * Time :
 * Remark : Toppos 지사 택분실게시판 ListDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagNoticeViewDto {

    private String htSubject; // 제목
    private String htContent; // 내용
    private String insert_id;
    private LocalDateTime insertDateTime;

    public String getInsertDateTime() {
        return insertDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }
}
