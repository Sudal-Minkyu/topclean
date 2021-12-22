package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import lombok.*;

import javax.persistence.*;

/**
 * @author Minkyu
 * Date : 2021-12-22
 * Time :
 * Remark : Toppos 접수세부 검품정보 테이블
 */
@Entity
@Data
@EqualsAndHashCode()
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="fs_request_inspect")
public class Inspeot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fi_id")
    private Long Id;

    @ManyToOne(targetEntity = RequestDetail.class,fetch = FetchType.LAZY)
    @JoinColumn(name="fd_id")
    private RequestDetail fdId; // 접수 세부테이블 ID

}
