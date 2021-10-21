package com.broadwave.toppos.Account;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-10-08
 * Time :
 * Remark : Toppos 사용자계정 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(unique = true,name="user_id")
    private String userid;

    @Column(name="user_password")
    private String password;

    @Column(name="user_name")
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name="user_role")
    private AccountRole role;

//    @Builder
//    public Account(String userid, String password, String username, AccountRole role) {
//        this.userid = userid;
//        this.password = password;
//        this.username = username;
//        this.role = role;
//    }

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="insert_id")
    private String insert_id;

}
