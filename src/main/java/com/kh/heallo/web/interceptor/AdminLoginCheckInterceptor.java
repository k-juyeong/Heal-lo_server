package com.kh.heallo.web.interceptor;

import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.svc.MemberSVC;
import com.kh.heallo.web.member.session.LoginMember;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import com.kh.heallo.web.session.Session;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class AdminLoginCheckInterceptor implements HandlerInterceptor {

    private final MemberSVC memberSVC;

    //로그인 상태 확인
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
        Member member = memberSVC.findById(loginMember.getMemno());

        if (!member.getMemcode().equals("ADMIN")) {

            if (request.getHeader("Accept").equals(MediaType.APPLICATION_JSON_VALUE)) {

                ResponseMsg responseMsg = new ResponseMsg()
                        .createHeader(StatusCode.NOT_LOGIN_ERROR)
                        .setData("msg", "관리자 권한이 필요합니다.");

                request.setAttribute(Session.NOT_LOGIN.name(), new ResponseEntity<>(responseMsg, HttpStatus.BAD_REQUEST));


            } else {
                response.sendRedirect("/");
            }
        }

        //로그인 세션이 존재하면 다음 인터셉터 or 정상로직
        return true;
    }
}
