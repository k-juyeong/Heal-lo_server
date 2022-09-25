package com.kh.heallo.web;

import com.kh.heallo.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //인터셉터 추가
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> whiteList = new ArrayList<>();
        whiteList.add("/resource/**");
        whiteList.add("*.ico");
        whiteList.add("/");
        whiteList.add("/error");
        whiteList.add("/members/join");
        whiteList.add("/members/login");
        whiteList.add("/facilities/**");
        whiteList.add("/reviews/*/total");
        whiteList.add("/reviews/*/list");
        whiteList.add("/reviews/*/new-images");
        whiteList.add("/calendar/**");
        whiteList.add("/images/*/*");
        whiteList.add("/members/find_id_pw/**");

        whiteList.add("/boards/**");


        //로그인체크 인터셉터
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(whiteList);
    }
}
