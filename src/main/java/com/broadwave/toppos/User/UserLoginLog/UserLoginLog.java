package com.broadwave.toppos.User.UserLoginLog;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-14
 * Time :
 * Remark : Toppos 가맹점별 로그인 로그
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(UserLoginLogPK.class)
@Table(name="bs_franchise_login_log")
public class UserLoginLog {

    @Id
    @Column(name="fr_code")
    private String frCode; // 가맹점 코드 3자리

    @Id
    @Column(name="bl_login_dt")
    private String blLoginDt; // 로그인일자

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
