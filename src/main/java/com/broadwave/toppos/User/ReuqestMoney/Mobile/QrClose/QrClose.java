package com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-03-04
 * Time :
 * Remark : Toppos 가맹점 QR 마감 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_request_qr_close")
public class QrClose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fq_id")
    private Long id;

    @Column(name="fr_code")
    private String frCode; // 가맹점 코드 3자리

    @Column(name="fq_close_cnt")
    private Integer fqCloseCnt; // 수거건수

    @Column(name="insert_yyyymmdd")
    private String insertYyyymmdd; // 등록일자

    @Column(name="insert_dt")
    private LocalDateTime insertDt; // 등록시간

}
