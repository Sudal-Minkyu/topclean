package com.broadwave.toppos.User.Customer.CustomerDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : DongA
 * Date : 2022-05-30
 * Time :
 * Remark : 고객 성별 비중 dto
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerGenderRateDto {
    private String gender; // 성별
    private Long rate; // 비중

}
