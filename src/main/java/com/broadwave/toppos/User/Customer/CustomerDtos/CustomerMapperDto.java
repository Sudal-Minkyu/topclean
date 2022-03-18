package com.broadwave.toppos.User.Customer.CustomerDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2021-11-16
 * Time :
 * Remark : Toppos 고객테이블 MapperDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMapperDto {

    private Long bcId; // 고유ID값
    private String bcHp; // 휴대폰번호( "-" 빼고저장)
    private String bcName; // 고객명
    private String bcSex; // 성별( 0:남자, 1:여자)
    private String bcAddress; // 주소
    private String bcBirthday; // 생년월일(8글자)
    private String bcAge; // 연련대( 10:10대, 20:20대, 30:30대...80:80대)
    private String bcWeddingAnniversary; // 결혼기념일(8글자)
    private String bcGrade; // 고객등급( 일반-기본값 : 01, vip: 02, vvip: 03)
    private String bcValuation; // 고객평가( 별1개: 1, 별2개: 2, 별3개: 3, 별4개: 4, 별5개: 5)
    private String bcMessageAgree; // 문자수신동의(Y:동의, N: 미동의)
    private String bcAgreeType; // 동의타입 (온라인 : 1, 서면 : 2)
    private String bcSignImage; // 동의 사인이미지 Blob객체사용 없으면 Null 값으로 받기
    private String bcQuitYn; // 탈퇴여부
    private String bcRemark; // 특이사항

    public String getBcSignImage() {
        if(bcSignImage == null){
            return null;
        }else{
            return bcSignImage;
        }
    }

}
