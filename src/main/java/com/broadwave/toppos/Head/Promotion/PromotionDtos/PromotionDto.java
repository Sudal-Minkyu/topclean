package com.broadwave.toppos.Head.Promotion.PromotionDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark : Toppos 본사 행사 등록/삭제 할 데이터 SetDto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDto {

    private Long hpId; // 행사등록 마스터 ID
    private String hpType; // 행사유형 ( 일반행사:01, 1+1행사 : 02 , 2+1행사 : 03 )
    private String hpName; // 행사명칭
    private String hpStart; // 시작시간 ( 년월일시분, ex> 2022년07월02일 15시 30분 -> 202207021530)
    private String hpEnd; // 종료시간 ( 년월일시분, ex> 2022년07월02일 15시 30분 -> 202207021530)
    private String hpWeekend; // 적용요일( 일월화수목금토 -> ex> 화수목만 적용 0011100 )
    private String hpStatus; // 진행여부( 01:진행(기본값), 02:종료)
    private String hpContent; // 행사내용

}
