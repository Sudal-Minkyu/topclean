package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-01-04
 * Time :
 * Remark : Toppos 접수세부 검품정보 테이블 MapperDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspeotMapperDto {

    private Long fdId; // 마스터테이블  ID
    private Integer fiAddAmt; // 세탁 추가발생 비용
    private String fiType; // 검품 타입 , F : 가맹검품, B:지사검품(확인품)
    private String fiComment; // 검품 특이사항

    public Long getFdId() {
        return fdId;
    }

    public Integer getFiAddAmt() {
        return fiAddAmt;
    }

    public String getFiType() {
        return fiType;
    }

    public String getFiComment() {
        return fiComment;
    }
}
