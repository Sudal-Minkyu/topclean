package com.broadwave.toppos.User.Customer.CustomerDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : DongA
 * Date : 2022-05-30
 * Time :
 * Remark : 고객 나이 비중 dto
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerAgeRateDto {
    private String age; // 나이
    private Long rate; // 비중
}
