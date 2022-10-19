package com.broadwave.toppos.Error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-03-24
 * Time :
 * Remark
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMapperDto {

    private String erTitle;
    private String erMsg;

}
