package com.kh.heallo.web;

import com.kh.heallo.web.interceptor.AdminLoginCheckInterceptor;
import com.kh.heallo.web.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;
    private final AdminLoginCheckInterceptor adminLoginCheckInterceptor;

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
        whiteList.add("/members/find_id/**");
        whiteList.add("/members/find_pw/**");

        whiteList.add("/boards/**");

        //로그인체크 인터셉터
        registry.addInterceptor(loginCheckInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(whiteList);

        registry.addInterceptor(adminLoginCheckInterceptor)
                .order(2)
                .addPathPatterns("/admin/**");
    }

}
