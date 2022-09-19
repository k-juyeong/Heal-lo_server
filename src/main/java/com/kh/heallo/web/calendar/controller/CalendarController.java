package com.kh.heallo.web.calendar.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/calendar")

public class CalendarController {
  // 달력 메인 (전체)
  @GetMapping
  public String calendar() {
    return "calendar/calendarForm";
  }

  // 운동기록 등록화면
  @GetMapping("/{rdate}/add")
  public String addForm() {
    return "calendar/addForm";
  }

  // 운동기록 등록처리
  @PostMapping("/add")
  public void add() {

  }

  // 운동기록 조회
  @GetMapping("/{rdate}")
  public String findByDate() {
    return "calendar/dayForm";
  }

  // 운동기록 수정화면
  @GetMapping("/{rdate}/edit")
  public String editForm() {
    return "calendar/editForm";
  }

  // 운동기록 수정처리
  @PostMapping("/{rdate}/edit")
  public void edit() {

  }

  // 운동기록 삭제
  @GetMapping("/{rdate}/del")
  public void del() {

  }
}
