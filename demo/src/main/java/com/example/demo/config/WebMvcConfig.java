package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 부트의 서버에서 CORS 를 허용하는 방식은 상당히 간단하다.
// 이 클래스를 bean으로 등록해서 서버가 스타트 됨과 동시에 메모리에 올리고
// 설정하는 방법은 WebMvcConfigurer(Interface) 를 상속해서 corsXXX() 를 오버라이드 하면 된다.
// 이때 이 메서드에는 설정 정보를 등록하는 파라미터가 같이 오는데, 그 파라미터는 객체의 메서드(주로 setter) 를 이용해서 설정하면 된다.

//Config 객체를 Bean으로 등록하는 어노테이션
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    
    @Override
    public void addCorsMappings(CorsRegistry registry){
        //registry 객체 메서드를 이용해서 등록만 하면 된다.
        registry.addMapping("/**") //루트 path 밑의 모든 경로
        .allowedOrigins("http://localhost:3000") //처음에 어디에서 시작되었는지
        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") //어떤 메소드에서 호출할건지
        .allowedHeaders("*") //헤더
        .allowCredentials(true) //신뢰도 허용할건지
        .maxAge(3600); //3600초
    }
}
