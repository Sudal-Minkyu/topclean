package com.broadwave.toppos.User.Template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-04-15
 * Time :
 * Remark : Toppos 고객에게 문자메세지 보낼 템플릿 테이블
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDto {

    private Long ftId;
    private Long fmNum;
    private String fmSubject;
    private String fmMessage;

}
