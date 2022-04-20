package com.broadwave.toppos.Head.Franchise.FranchiseDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * @author Minkyu
 * Date : 2022-04-19
 * Time :
 * Remark : Toppos 가맹점 TagType,No 호출 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseTagDataDto {

    private String frTagNo; // 가맹점 택번호 2자리 or 3자리
    private String frTagType; // 2: 2자리, 3: 3자리

}
