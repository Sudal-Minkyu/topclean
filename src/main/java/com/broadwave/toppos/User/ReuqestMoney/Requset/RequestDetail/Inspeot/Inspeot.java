package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-22
 * Time :
 * Remark : Toppos 접수세부 검품정보 테이블 엔티티
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
    private Long id;

    @ManyToOne(targetEntity = RequestDetail.class,fetch = FetchType.LAZY)
    @JoinColumn(name="fd_id")
    private RequestDetail fdId; // 접수 세부테이블 ID

    @Column(name="fr_code")
    private String frCode; // 가맹점 코드 3자리

    @Column(name="br_code")
    private String brCode; // 지점 코드 2자리

    @Column(name="fi_type")
    private String fiType; // 검품 타입 , F : 가맹검품, B:지사검품(확인품)

    @Column(name="fi_comment")
    private String fiComment; // 검품 특이사항

    @Column(name="fi_add_amt")
    private Integer fiAddAmt; // 세탁 추가발생 비용

    @Column(name="fi_photo_yn")
    private String fiPhotoYn; // 검품 사진등록여부 (Y : 사진있음 , fs_photo_file 테이블에존재, N : 없음)

    @Column(name="fi_send_msg_yn")
    private String fiSendMsgYn; // 메세지전송여부 Y , N  (기본값N)

    @Column(name="fi_customer_confirm")
    private String fiCustomerConfirm; // 고객 수락여부 ( 1: 미확인(기본값), 2:고객수락, 3:고객거부

    @Column(name="fi_progress_state_dt")
    private LocalDateTime fiProgressStateDt; // 고객진행여부 변경상태

    @Column(name="fi_message")
    private String fiMessage; // 고객 전송메세지

    @Column(name="fi_message_send_dt")
    private LocalDateTime fiMessageSendDt; // 메세지 송신시간

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modify_date;

}
