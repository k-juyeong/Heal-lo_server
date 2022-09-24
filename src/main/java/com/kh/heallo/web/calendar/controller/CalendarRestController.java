package com.kh.heallo.web.calendar.controller;

import com.kh.heallo.domain.calendar.Calendar;
import com.kh.heallo.domain.calendar.svc.CalendarSVC;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PathVariable String month
  ) {
    List<Calendar> monthly = calendarSVC.monthly(year, month);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("monthly", monthly);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);

  }

}
