package com.broadwave.toppos.Head.Franchise.FranchiseDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-03-22
 * Time :
 * Remark : Toppos 현재 가맹점의 멀티스크린여부를 가져오는 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseMultiscreenDto {

    private String frMultiscreenYn; // 멀티스크린 사용여부

}
