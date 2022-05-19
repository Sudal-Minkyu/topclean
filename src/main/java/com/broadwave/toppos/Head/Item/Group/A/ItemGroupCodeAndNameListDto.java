package com.broadwave.toppos.Head.Item.Group.A;

import lombok.*;

/**
 * @author : DongA
 * Date : 2022-05-13
 * Time :
 * Remark : 사용여부에 따른 대분류코드 및 이름 호출
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemGroupCodeAndNameListDto {

    private String bgItemGroupcode; // 대분류코드

    private String bgName; // 대문류명칭

}
