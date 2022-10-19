package com.broadwave.toppos.Jwt.token;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Account.AccountService;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseInfoDto;
import com.broadwave.toppos.Head.HeadService.HeadService;
import com.broadwave.toppos.Jwt.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private final AccountService accountService;
    private final HeadService headService;

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 12; // 12시간으로 변경 12/03  //  1000 * 60 * 60 * 24;  24시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    private final Key key;

    @Autowired
    public TokenProvider(@Value("${spring.jwt.secret}") String secretKey,
                         AccountService accountService, HeadService headService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accountService = accountService;
        this.headService = headService;
    }

    public TokenDto generateTokenDto(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Optional<Account> optionalAccount = accountService.findByUserid(authentication.getName());

        long now = (new Date()).getTime();

        FranchiseInfoDto franchiseInfoDto = new FranchiseInfoDto();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = null;
        String frbrCode = "";
        if(optionalAccount.isPresent()) {
            if(!optionalAccount.get().getFrCode().equals("not")){
                franchiseInfoDto = headService.findByFranchiseInfo(optionalAccount.get().getFrCode());
                if(franchiseInfoDto.getBrCode() == null){
                    frbrCode = "소속지사없음";
                }else{
                    frbrCode = franchiseInfoDto.getBrCode();
                }
            }

            accessToken = Jwts.builder()
                    .claim("frCode", optionalAccount.get().getFrCode())
                    .claim("frTagNo", franchiseInfoDto.getFrTagNo())
                    .claim("frTagType", franchiseInfoDto.getFrTagType())
                    .claim("brCode", optionalAccount.get().getBrCode())
                    .claim("frbrCode", frbrCode)
                    .setSubject(authentication.getName())       // payload "sub": "name"
                    .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                    .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                    .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                    .compact();
        }

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .frbrCode(frbrCode)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
