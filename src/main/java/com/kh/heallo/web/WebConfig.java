package com.kh.heallo.web;

import com.kh.heallo.web.interceptor.LogInterceptor;
import com.kh.heallo.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //인터셉터 추가
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //로그출력 인터셉터
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/img/**","/js/**", "/*.ico","/error");

        //로그인체크 인터셉터
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**", "/img/**", "/js/**", "*.ico", "/error", "/",
                        "/members/join", "/members/login", "/facilities/**",
                        "/reviews/*/total", "/reviews/*/list",
                        "/login","/logout"
                );
    }
}
