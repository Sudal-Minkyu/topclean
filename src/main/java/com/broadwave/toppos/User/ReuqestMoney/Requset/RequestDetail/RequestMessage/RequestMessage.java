package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestMessage;

import com.broadwave.toppos.User.Customer.Customer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-03-14
 * Time :
 * Remark : Toppos 접수세부 가맹점입고 or 강제입고시 고객에게 메세지보낼때 insert하는 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "fmId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_request_input_message")
public class RequestMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fm_id")
    private Long fmId;

    @Column(name="fr_code")
    private String frCode; // 가맹점 코드 3자리

    @Column(name="fr_no")
    private String frNo; // 접수코드

    @ManyToOne(targetEntity = Customer.class,fetch = FetchType.LAZY)
    @JoinColumn(name="bc_id")
    private Customer bcId; // 고객테이블 ID

    @Column(length = 100000, name="fm_message")
    private String fmMessage; // 고객 전송메세지

    @Column(name="fm_message_yn")
    private String fmMessageYn; // 메세지 송신시간

    @Column(name="insert_yyyymmdd")
    private String insertYyyymmdd;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
