package com.broadwave.toppos.User.ReuqestMoney.SaveMoney;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.Payment;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-07
 * Time :
 * Remark : Toppos 고객 적립금내역 테이블
 */
@Entity
@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_saved_money")
public class SaveMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fs_id")
    private Long id; // 고유값 ID

    @ManyToOne(targetEntity = Customer.class,fetch = FetchType.LAZY)
    @JoinColumn(name="bc_id")
    private Customer bcId; // 고객 ID값

    @ManyToOne(targetEntity = Payment.class,fetch = FetchType.LAZY)
    @JoinColumn(name="fp_id")
    private Payment fpId; // 고객 ID값

    @Column(name="fs_yyyymmdd")
    private String fsYyyymmdd; // 시행일자

    @Column(name="fs_type")
    private String fsType; // 적립유형

    @Column(name="fs_close")
    private String fsClose; // 마감상태

    @Column(name="fs_amt")
    private Integer fsAmt; // 적립금액 or 사용금액

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insert_date;

}
