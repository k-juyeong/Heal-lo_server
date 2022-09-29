package com.kh.heallo.web.calendar.controller;

import com.kh.heallo.domain.calendar.Calendar;
import com.kh.heallo.domain.calendar.svc.CalendarSVC;
import com.kh.heallo.web.session.LoginMember;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import com.kh.heallo.web.session.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/calendars")
public class CalendarRestController {

  private final CalendarSVC calendarSVC;

  // 달력 화면에 운동기록 표시
  @GetMapping("/{year}/{month}")
  public ResponseEntity<ResponseMsg> calendar(
    @PathVariable String year,
    @PathVariable String month,
    HttpServletRequest request
  ) {
    // 회원번호
    Long memno = 0L;
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
      memno = loginMember.getMemno();
    }
    log.info("memno={}", memno);
    List<Calendar> monthly = calendarSVC.monthly(year, month, memno);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("monthly", monthly);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);

  }

}
