package com.broadwave.toppos.Manager.TagNotice;

import lombok.*;

import javax.persistence.*;
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
public class TagNoticeListDto {

    private String htSubject; // 제목
    private String insert_id;
    private LocalDateTime insertDateTime;

    public String getInsertDateTime() {
        return insertDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
