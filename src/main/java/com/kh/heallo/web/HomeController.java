package com.kh.heallo.web;

import com.kh.heallo.domain.member.Member;
import com.kh.heallo.web.member.session.LoginMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class HomeController {

    @GetMapping
    public String home() {

        return "index";
    }

    //잠시 테스트
    @GetMapping("/login")
    public String loginView() {

        return "test";
    }
    @PostMapping("/login")
    public String login(@RequestParam(value = "requestURI", required = false) String uri, HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        if (session != null) {
            session.setAttribute("loginMember", new LoginMember("test", "test1"));
        }

        if (uri != null) {
            log.info("uri {}", uri);
            return "redirect:" + uri;
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
