package com.broadwave.toppos.Manager.TagNotice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-02-04
 * Time :
 * Remark : Toppos 지사 택분실게시판 이전글,다음글 ViewSubDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagNoticeViewSubDto {

    private Long subId;
    private String subSubject;
    private LocalDateTime subInsertDateTime;

    public String getSubInsertDateTime() {
        return subInsertDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }

}
