package com.broadwave.toppos.Jwt.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String frbrCode; // 가맹점일시 소속된 지사가 있는지 확인하는 키
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}
