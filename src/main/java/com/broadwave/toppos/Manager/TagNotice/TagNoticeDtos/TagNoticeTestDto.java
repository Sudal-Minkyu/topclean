package com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author InSeok
 * Date : 2022-02-15
 * Remark :
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TagNoticeTestDto {
    private String subject; // 제목
    private String insert_id;
    private BigDecimal numOfCount;


}
