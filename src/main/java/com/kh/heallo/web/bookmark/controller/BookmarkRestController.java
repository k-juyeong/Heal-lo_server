package com.kh.heallo.web.bookmark.controller;

import com.kh.heallo.domain.bookmark.svc.BookmarkSVC;
import com.kh.heallo.web.session.LoginMember;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import com.kh.heallo.web.session.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
@Slf4j
public class BookmarkRestController {

    private final BookmarkSVC bookmarkSVC;

    //등록 or 삭제
    @PatchMapping("/{fcno}")
    public ResponseEntity<ResponseMsg> replace(
            HttpServletRequest request,
            @PathVariable("fcno") Long fcno
    ) {

        //회원 조회
        if (request.getAttribute(Session.NOT_LOGIN.name()) != null) {
            ResponseEntity<ResponseMsg> notLoginResponseEntity = (ResponseEntity) request.getAttribute(Session.NOT_LOGIN.name());
            return notLoginResponseEntity;
        }

        //회원 조회 성공
        HttpSession session = request.getSession();
        LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());

        //즐겨찾기 상태변경
        Boolean status = bookmarkSVC.replace(loginMember.getMemno(), fcno);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .setData("status",status)
                .createHeader(StatusCode.SUCCESS);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
}
