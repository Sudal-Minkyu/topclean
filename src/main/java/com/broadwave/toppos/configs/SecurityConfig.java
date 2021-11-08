package com.broadwave.toppos.configs;

import com.broadwave.toppos.Jwt.token.JwtAccessDeniedHandler;
import com.broadwave.toppos.Jwt.token.JwtAuthenticationEntryPoint;
import com.broadwave.toppos.Jwt.token.JwtSecurityConfig;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.handler.TaskImplementingLogoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : 기본 시큐리티셋팅
 */
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http
                .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제하겠습니다.
                .csrf().disable() // csrf 보안 토큰 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
                .and()

                // exception handling 할 때 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()
                .antMatchers("/","/assets/**","/login","/favicon.ico","/error/**","/test/**","api/test/**","/auth/**","/user/**","/admin/**","/manager/**","/head/**").permitAll()
                .antMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/account/**").hasAnyAuthority("ROLE_ADMIN","ROLE_HEAD")
                .antMatchers("/api/user/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                .antMatchers("/api/manager/**").hasAnyAuthority("ROLE_MANAGER","ROLE_NORMAL","ROLE_ADMIN")
                .antMatchers("/api/head/**").hasAnyAuthority("ROLE_HEAD","ROLE_ADMIN") // "ROLE_CALCULATE" 회계관리는 일단 제외 -> 후추 회계관리가 사용되는 API에 직접연결하기
                .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요

                // 로그아웃기능 만들어야함
//                .and()
//                .formLogin().loginPage("/login").failureUrl("/login?error").permitAll()
//                .defaultSuccessUrl("/admin/index")
//                .and()
//                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .addLogoutHandler(new TaskImplementingLogoutHandler()).permitAll().logoutSuccessUrl("/admin");

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }



}
