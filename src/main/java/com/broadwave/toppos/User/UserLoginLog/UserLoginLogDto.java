package com.broadwave.toppos.User.UserLoginLog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-03-03
 * Time :
 * Remark : Toppos 지사 메인페이지 그래프용 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginLogDto {

    private String frCode; // 가맹점 코드 3자리

}
