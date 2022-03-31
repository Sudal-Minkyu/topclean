package com.broadwave.toppos.User.ReuqestMoney.Requset.Find;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-03-30
 * Time :
 * Remark : Toppos 물품찾기 테이블
 */
@Entity
@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_request_find")
public class Find {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ff_id")
    private Long id; // 고유값 ID

    @ManyToOne(targetEntity = RequestDetail.class,fetch = FetchType.LAZY)
    @JoinColumn(name="fd_id")
    private RequestDetail fdId; // 접수 마스터테이블 ID값

    @Column(name="fr_code")
    private String frCode;

    @Column(name="br_code")
    private String brCode;

    @Column(name="ff_yyyymmdd")
    private String ffYyyymmdd; // 물품찾기 등록일

    @Column(name="ff_state")
    private String ffState; // 01:요청, 02:지사확인, 기본값 01

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insert_date;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modify_date;

}
