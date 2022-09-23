package com.kh.heallo.web.calendar.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/calendar")
public class CalendarRestController {
  // 달력 화면에 운동기록 표시
  @GetMapping
  public void calendar() {

  }

}
