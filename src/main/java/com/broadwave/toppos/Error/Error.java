package com.broadwave.toppos.Error;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-03-24
 * Time :
 * Remark : Toppos 에러모음 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="er_table")
public class Error {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="er_id")
    private Long id;

    @Column(name="er_title")
    private String erTitle;

    @Column(length = 100000, name="er_msg")
    private String erMsg;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
