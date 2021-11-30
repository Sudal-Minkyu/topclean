package com.broadwave.toppos.User.Customer;

import lombok.*;
import org.hibernate.type.BlobType;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-16
 * Time :
 * Remark : Toppos 고객 테이블(가맹점전용)
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bc_id")
    private Long id;

    @Column(name="fr_code")
    private String frCode; // 가맹점코드 3자리

    @Column(unique = true, name="bc_hp")
    private String bcHp; // 휴대폰번호( "-" 빼고저장)

    @Column(name="bc_name")
    private String bcName; // 고객명

    @Column(name="bc_sex")
    private String bcSex; // 성별( 0:남자, 1:여자)

    @Column(name="bc_address")
    private String bcAddress; // 주소

    @Column(name="bc_birthday")
    private String bcBirthday; // 생년월일(8글자)

    @Column(name="bc_age")
    private String bcAge; // 연련대( 10:10대, 20:20대, 30:30대...80:80대)

    @Column(name="bc_grade")
    private String bcGrade; // 고객등급( 일반-기본값 : 01, vip: 02, vvip: 03)

    @Column(name="bc_valuation")
    private String bcValuation; // 고객평가( 별1개: 1, 별2개: 2, 별3개: 3, 별4개: 4, 별5개: 5)

    @Column(name="bc_message_agree")
    private String bcMessageAgree; // 문자수신동의(Y:동의, N: 미동의)

    @Column(name="bc_message_agree_dt")
    private LocalDateTime bcMessageAgreeDt; // 동의 또는 거부 시간

    @Column(name="bc_agree_type")
    private String bcAgreeType; // 동의타입 (온라인 : 1, 서면 : 2)

    @Column(name="bc_sign_image")
    private BlobType bcSignImage; // 동의 사인이미지 Blob객체사용

    @Column(name="bc_remark")
    private String bcRemark; // 특이사항

    @Column(name="bc_quit_yn")
    private String bcQuitYn; // 탈퇴여부 (기본값:N)

    @Column(name="bc_quit_date")
    private LocalDateTime bcQuitDate; // 탈퇴일시

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
