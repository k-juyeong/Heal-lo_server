package com.kh.heallo.web.bookmark;

import com.kh.heallo.domain.bookmark.Bookmark;
import com.kh.heallo.domain.bookmark.svc.BookmarkSVC;
import com.kh.heallo.web.member.session.LoginMember;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import com.kh.heallo.web.session.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {

    private final BookmarkSVC bookmarkSVC;

    @GetMapping
    public ResponseEntity findBookmark(
            HttpServletRequest request
    ) {
        //회원 조회
        if (request.getAttribute(Session.NOT_LOGIN.name()) != null) {
            ResponseEntity<ResponseMsg> notLoginResponseEntity = (ResponseEntity) request.getAttribute(Session.NOT_LOGIN.name());
            return notLoginResponseEntity;
        }

        //회원 조회 성공
        HttpSession session = request.getSession();
        LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());

        //즐겨찾기 상태확인
        List<Bookmark> bookmarkList = bookmarkSVC.findBookmarkListByMemno(loginMember.getMemno());

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .setData("bookmarks",bookmarkList)
                .createHeader(StatusCode.SUCCESS);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

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
