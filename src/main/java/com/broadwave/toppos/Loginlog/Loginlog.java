package com.broadwave.toppos.Loginlog;

import com.broadwave.toppos.Account.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-03-29
 * Time :
 * Remark : Toppos 공통 로그인기록 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_login_log")
public class Loginlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="login_id")
    private Long id;

    @ManyToOne(targetEntity = Account.class,fetch = FetchType.LAZY)
    @JoinColumn(name="account_id")
    private Account account; // 접수 세부테이블 ID

    @Column(name="user_id")
    private String userid; // 유저아이디

    @Column(name="login_ip")
    private String loginIp; // 로그인 IP

    @Column(name="success_yn")
    private String succcessYn; // 로그인 성공여부

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
