package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.MessageHistory;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.Inspeot;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-02-04
 * Time :
 * Remark : Toppos 가맹점 메세지 송신이력 엔티티
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_message_history")
public class MessageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fm_id")
    private Long fmId;

    @ManyToOne(targetEntity = Customer.class,fetch = FetchType.LAZY)
    @JoinColumn(name="bc_id")
    private Customer bcId; // 고객

    @Column(name="fm_type")
    private String fmType; // 검품:01, 자동:02, 수동: 03

    @ManyToOne(targetEntity = Inspeot.class,fetch = FetchType.LAZY)
    @JoinColumn(name="fi_id")
    private Inspeot fiId; // fmType이 01일 경우에만

    @Column(name="fr_code")
    private String frCode; // 가맹점코드 3자리

    @Column(name="br_code")
    private String brCode; // 소속 지사코드 2자리

    @Column(length = 100000, name="fm_message")
    private String fmMessage; // 메세지 내용

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
