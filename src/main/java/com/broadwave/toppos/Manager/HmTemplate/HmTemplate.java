package com.broadwave.toppos.Manager.HmTemplate;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-04-26
 * Time :
 * Remark : Toppos 지사/본사용 문자메세지 보낼 템플릿 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "hmId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="hb_message_template")
public class HmTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hm_id")
    private Long hmId;

    @Column(name="hm_num")
    private Long hmNum;

    @Column(name="br_code")
    private String brCode; // 지사코드 2자리 or 본사코드

    @Column(name="hm_subject")
    private String hmSubject;

    @Column(length = 100000, name="hm_message")
    private String hmMessage;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
