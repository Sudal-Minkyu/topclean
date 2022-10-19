package com.broadwave.toppos.User.Template;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-04-15
 * Time :
 * Remark : Toppos 고객에게 문자메세지 보낼 템플릿 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "ftId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_message_template")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ft_id")
    private Long ftId;

    @Column(name="fm_num")
    private Long fmNum;

    @Column(name="fr_code")
    private String frCode; // 가맹점코드 3자리

    @Column(name="fm_subject")
    private String fmSubject;

    @Column(length = 100000, name="fm_message")
    private String fmMessage;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
