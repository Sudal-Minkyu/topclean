package com.broadwave.toppos.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : 기본 시큐리티셋팅
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/eva/**").hasAnyRole("ADMIN","USER")
//                .antMatchers("/ltd/**").hasAnyRole("ADMIN","USER")
//                .antMatchers("/facility/**").hasAnyRole("ADMIN","USER")
//                .antMatchers("/env/**").hasAnyRole("ADMIN","USER")
//                .antMatchers("/mypage").hasAnyRole("ADMIN","USER")
                .antMatchers("/","/assets/**","/login","/logout","/favicon.ico").permitAll()
                .anyRequest().permitAll()

//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//                .logout()
//                .logoutSuccessUrl("/")
//                .permitAll()
        //.and()
        //.csrf().disable()
        ;



    }

//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // CSRF 설정 Disable
//        http
//                .csrf().disable()
//
//                // exception handling 할 때 만든 클래스를 추가
//                .exceptionHandling()
//
//                // jdbc를 위한 설정을 추가
//                .and()
//                .headers()
//                .frameOptions()
//                .sameOrigin()
//
//                // 시큐리티는 기본적으로 세션을 사용
//                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        // 로그인 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
////                .and()
////                .authorizeRequests()
////                .antMatchers("/api/**").permitAll()
////                .anyRequest().authenticated();   // 나머지 API 는 전부 인증 필요
//    }


}
