package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.JwtAuthenicationFilter;

import lombok.RequiredArgsConstructor;

//Security 필터 config 설정 클래스
//특정 URL에 대해서 인증없이 모두 접근 가능하도록 설정하고, 나중에 token 인증을 할 수 있도록 filter를 추가하는 작업 예정

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    

    @Autowired
    private JwtAuthenicationFilter jwtAuthenicationFilter;
    //아래 메서드는 SecurityFilterChain을 리턴하는 메서드 정의
    //WebToken secure filter 를 리턴해서 스프링에 적용하기 위해서
    //이때 파라미터로 오는 객체를 이용해서 Secure 설정을 제어할수도 있다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        
        //기본인증 해제
        http.httpBasic()
            .disable()
            .csrf()
            .disable()
            .cors()//cors 허용
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션 사용 못하게 막기
            .and()
            .authorizeHttpRequests()//조건 별로 요청 허용 //제한 설정
            .requestMatchers("/", "/auth/**").permitAll()//root와 auth 하위의 모든 패스는 인증없이 접근 가능하다.
            .anyRequest()
            .authenticated()
            .and()
            .addFilterBefore(jwtAuthenicationFilter, UsernamePasswordAuthenticationFilter.class)//JWT 토큰을 usernamepassword 필터 전에 끼워넣는다.
            .exceptionHandling();

            return http.build();
    }

}
