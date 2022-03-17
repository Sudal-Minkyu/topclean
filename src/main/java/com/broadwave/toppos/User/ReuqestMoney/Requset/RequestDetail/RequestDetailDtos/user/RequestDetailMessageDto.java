package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user;

import com.broadwave.toppos.User.Customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-03-16
 * Time :
 * Remark : Toppos 가맹점 접수세부 메세지용 상품이름호출 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailMessageDto {

    private Long frId; // 접수 ID값

    private Customer customer;
    private String frName; // 가맹점명
    private String frTelNo; // 가맹점번호
    private Integer frQty; // 수량
    private Integer frTotalAmount; // 총 접수금액
    private Integer frPayAmount; // 총 결제금액
    private String bgName; // 대분류명
    private String biName; // 상품명

}
