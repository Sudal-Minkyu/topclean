package com.broadwave.toppos.User.UserLogoutLog;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-05-12
 * Time :
 * Remark : Toppos 가맹점별 로그아웃 로그
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(UserLogoutLogPK.class)
@Table(name="bs_franchise_logout_log")
public class UserLogoutLog {

    @Id
    @Column(name="fr_code")
    private String frCode; // 가맹점 코드 3자리

    @Id
    @Column(name="bl_logout_dt")
    private String blLogoutDt; // 로그아웃 일자

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
