package com.broadwave.toppos.Manager.HmTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * @author Minkyu
 * Date : 2022-04-26
 * Time :
 * Remark : Toppos 고객에게 문자메세지 보낼 템플릿 테이블
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HmTemplateDto {

    private Long hmId;
    private Long hmNum;
    private String hmSubject;
    private String hmMessage;

}
