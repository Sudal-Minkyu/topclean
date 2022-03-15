package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user;

import com.broadwave.toppos.User.Customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author Minkyu
 * Date : 2022-03-14
 * Time :
 * Remark : Toppos 가맹점입고시 고객에게 보내는 메세지 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailInputMessageDto {
    private String frCode;
    private String frNo;
    private BigInteger bcId;
    private Integer frQty; // 건수
    private String bgName; // 대분류명
    private String biName; // 상품명

    public Long getBcId() {
        return Long.parseLong(String.valueOf(bcId));
    }
}
