package com.broadwave.toppos.Head.Promotion;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark : Toppos 본사 행사마스터 테이블
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "hb_promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hp_id")
    private Long hpId; // 행사등록 마스터 ID

    @Column(name = "hp_input_type")
    private String hpInputType; // 본사 : HR, 지사에서등록시 지사코드 두자리

    @Column(name = "hp_type")
    private String hpType; // 행사유형 ( 일반행사:01, 1+1행사 : 02 , 2+1행사 : 03 )

    @Column(name = "hp_name")
    private String hpName; // 행사명칭

    @Column(name = "hp_start")
    private String hpStart; // 시작시간 ( 년월일시분, ex> 2022년07월02일 15시 30분 -> 202207021530)

    @Column(name = "hp_end")
    private String hpEnd; // 종료시간 ( 년월일시분, ex> 2022년07월02일 15시 30분 -> 202207021530)

    @Column(name = "hp_weekend")
    private String hpWeekend; // 적용요일( 일월화수목금토 -> ex> 화수목만 적용 0011100 )

    @Column(name = "hp_status")
    private String hpStatus; // 진행여부( 01:진행(기본값), 02:종료)

    @Column(length = 100000, name="hp_content")
    private String hpContent; // 행사내용

    @Column(name = "hp_yyyymmdd")
    private String hpYyyymmdd; // 등록일자

    @Column(name = "insert_id")
    private String insert_id;

    @Column(name = "insert_date")
    private LocalDateTime insertDateTime;

    @Column(name = "modify_id")
    private String modify_id;

    @Column(name = "modify_date")
    private LocalDateTime modifyDateTime;

}
