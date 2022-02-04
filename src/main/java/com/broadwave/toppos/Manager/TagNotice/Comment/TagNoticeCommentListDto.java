package com.broadwave.toppos.Manager.TagNotice.Comment;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-01-28
 * Time :
 * Remark : Toppos 택분실 게시판 댓글 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagNoticeCommentListDto {

    private Long hcId;
    private String name;
    private String hcComment;
    private LocalDateTime modifyDt;
    private String type;
    private Long preId;
    private String isWritter;

    public String getModifyDt() {
        return modifyDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
