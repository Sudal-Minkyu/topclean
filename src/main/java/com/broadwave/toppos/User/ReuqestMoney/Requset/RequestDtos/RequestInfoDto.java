package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos;

import com.broadwave.toppos.User.Customer.Customer;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-07
 * Time :
 * Remark : Toppos 가맹점 접수마스트 RequestInfoDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfoDto {

    private Long id;
    private String frNo; // 접수코드
    private Customer bcId; // 고객 ID값
    private String frCode; // 가맹점코드
    private String brCode; // 지점코드
    private String frYyyymmdd; // 접수일자
    private Integer frQty; // 접수건수
    private Integer frNormalAmount; // 접수금액
    private Integer frDiscountAmount; // 할인금액
    private Integer frTotalAmount; // 합계금액
    private Integer frPayAmount; // 결제금액
    private String frUncollectYn; // 미수여부( 합계금액 > 결제금액 이면 Y, else N)
    private String frConfirmYn; // 임시저장여부플래그 Y만 정식데이터
    private String frRefType; // 접수타입(01:일반, 02:무인보관함, 03:배송APP)
    private String frRefBoxCode; // 무인보관함 연계시 무인보관함 접수번호
    private String fr_insert_id;
    private LocalDateTime fr_insert_date;
    private String modity_id;
    private LocalDateTime modity_date;

}
