package com.kh.heallo.web.interceptor;

import com.kh.heallo.domain.member.Member;
import com.kh.heallo.web.member.session.LoginMember;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    //로그인 상태 확인
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        LoginMember loginMember = null;

        if (session != null) {
            log.info("loginMember {}", session.getAttribute("loginMember"));
            loginMember = (LoginMember) session.getAttribute("loginMember");
        }

        //로그인 세션이 없으면 실행
        if(loginMember == null) {
            log.info("session {}",loginMember);

            //컨텐츠타입이 json이면 ResponseEntity 생성 후 넘겨줌
            if (request.getHeader("Accept").contains(MediaType.APPLICATION_JSON_VALUE)) {
                ResponseMsg responseMsg = new ResponseMsg()
                        .createHeader(StatusCode.NOT_LOGIN_ERROR);
                request.setAttribute("notLogin",new ResponseEntity<>(responseMsg,HttpStatus.BAD_REQUEST));

            //현재 페이지주소를 쿼리파라미터로 넘기고 로그인 페이지로 redirect
            } else {
                response.sendRedirect("/login" + "?requestURI=" + request.getRequestURI().toString());
                return false;
            }

        }

        //로그인 세션이 존재하면 다음 인터셉터 or 정상로직
        return true;
    }
}
