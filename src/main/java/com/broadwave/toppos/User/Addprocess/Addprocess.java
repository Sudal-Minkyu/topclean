package com.broadwave.toppos.User.Addprocess;

import com.broadwave.toppos.Head.Franchise.Franchise;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark : Toppos 상품 수선, 추가요금항목, 상용구항목 관련 테이블
 */
@Entity
@Data
@Builder
@EqualsAndHashCode(of = "baId")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_franchise_addprocess")
public class Addprocess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ba_id")
    private Long baId; // 고유값 ID

    @ManyToOne(targetEntity = Franchise.class,fetch = FetchType.LAZY)
    @JoinColumn(name="fr_id")
    private Franchise frId; // 가맹점 고유값 ID

    @Column(name="fr_code")
    private String frCode; // 가맹점 코드 3자리

    @Column(name="ba_type")
    private String baType; // 1: 수선항목, 2:추가요금항목, 3:상용구항목

    @Column(name="ba_sort")
    private Integer baSort; // 정렬순서

    @Column(name="ba_name")
    private String baName; // 명칭

    @Column(name="ba_remark")
    private String baRemark; // 비고

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
