package com.broadwave.toppos.configs;

import com.broadwave.toppos.jwt.exception.ExceptionHandlerFilter;
import com.broadwave.toppos.jwt.token.JwtAuthenticationEntryPoint;
import com.broadwave.toppos.jwt.utils.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : 기본 시큐리티셋팅
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private ExceptionHandlerFilter exceptionHandlerFilter;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configGlobal(AuthenticationManagerBuilder auth) throws Exception{
        // 일치하는 자격증명을 위해 사용자를 로드할 위치를 알수 있도록
        // AuthenticationManager를 구성한다.
        // BCryptPasswordEncoder를 이용
        auth.userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .cors().disable()
                .csrf().disable()
                // 이 요청은 인증을 하지 않는다.
                .authorizeRequests().antMatchers(
                "/api/authenticate",
                "/api/account/savetest","/admin/**",
                "/","/assets/**","/login","/favicon.ico","/gridtest")
                .permitAll()
                // 다른 모든 요청은 인증을 한다.
                .anyRequest().authenticated().and()
                // 상태없는 세션을 이용하며, 세션에 사용자의 상태를 저장하지 않는다.
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .formLogin().disable()
                .headers().frameOptions().disable();
                // 모든 요청에 토큰을 검증하는 필터를 추가한다.
                http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
                http.addFilterBefore(exceptionHandlerFilter, JwtRequestFilter.class);
    }

}
