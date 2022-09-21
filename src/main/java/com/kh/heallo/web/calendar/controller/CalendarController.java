package com.kh.heallo.web.calendar.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.kh.heallo.domain.calendar.Calendar;
import com.kh.heallo.domain.calendar.svc.CalendarSVC;
import com.kh.heallo.web.calendar.dto.AddForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

  private final CalendarSVC calendarSVC;

  // 달력 메인 (전체)
  @GetMapping
  public String calendar() {
    return "calendar/calendarForm";
  }

  // 운동기록 등록화면
  @GetMapping("/{rdate}/add")
  public String addForm(@PathVariable String rdate, Model model) {
    model.addAttribute("rdate", rdate);
    return "calendar/addForm";
  }

  // 운동기록 등록처리
  @PostMapping("/add")
  public String add(
      @Valid @ModelAttribute("form") AddForm addForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    // 기본 검증
    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "calendar/addForm";
    }

    // 이미지 첨부 코드



    Calendar calendarRecord = new Calendar();
    calendarRecord.setCdContent(addForm.getCdContent());
    calendarRecord.setCdRDate(addForm.getCdRDate());

    String rdate = calendarRecord.getCdRDate();
    calendarSVC.save(rdate, calendarRecord);

    redirectAttributes.addAttribute("rdate", rdate);
    return "redirect:/calendar/{rdate}";
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
