package com.kh.heallo.web.calendar.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.kh.heallo.domain.calendar.Calendar;
import com.kh.heallo.domain.calendar.svc.CalendarSVC;
import com.kh.heallo.web.calendar.dto.AddForm;
import com.kh.heallo.web.calendar.dto.DayForm;
import com.kh.heallo.web.calendar.dto.EditForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

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
  @PostMapping("/{rdate}/add")
  public String add(AddForm addForm) {

    // 기본 검증

    // 오브젝트 검증
    // 첨부파일 & 내용 아무것도 없을 때

    Calendar calendarRecord = new Calendar();
    String rdate = addForm.getCdRDate();
    calendarRecord.setCdRDate(rdate);
    calendarRecord.setCdContent(addForm.getCdContent());


    calendarSVC.save(rdate, calendarRecord);
    return "redirect:/calendar/"+rdate;

    // 이미지 첨부 코드



//    Calendar calendarRecord = new Calendar();
//    calendarRecord.setCdContent(addForm.getCdContent());
//    calendarRecord.setCdRDate(addForm.getCdRDate());
//
//    String rdate = calendarRecord.getCdRDate();
//    calendarSVC.save(rdate, calendarRecord);
//
//    redirectAttributes.addAttribute("rdate", rdate);
//    return "redirect:/calendar/{rdate}";
  }

  // 운동기록 조회
  @GetMapping("/{rdate}")
  public String findByDate(
      @PathVariable String rdate,
      Model model
  ) {
    DayForm dayForm = new DayForm();
    Optional<Calendar> foundRecord = calendarSVC.findByDate(rdate);
    if (!foundRecord.isEmpty()) {
      dayForm.setCdContent(foundRecord.get().getCdContent());
      dayForm.setCdUDate(foundRecord.get().getCdUDate());
      dayForm.setCdCDate(foundRecord.get().getCdCDate());
      String cdRDate = foundRecord.get().getCdRDate().substring(0, 10);
      dayForm.setCdRDate(cdRDate);
    }
    model.addAttribute("form", dayForm);
    return "calendar/dayForm";
  }

  // 운동기록 수정화면
  @GetMapping("/{rdate}/edit")
  public String editForm(
      @PathVariable String rdate,
      Model model
  ) {
    EditForm editForm = new EditForm();
    Optional<Calendar> foundRecord = calendarSVC.findByDate(rdate);
    if (!foundRecord.isEmpty()) {
      BeanUtils.copyProperties(foundRecord.get(), editForm);
    }

    model.addAttribute("form", editForm);
    return "calendar/editForm";
  }

  // 운동기록 수정처리
  @PostMapping("/{rdate}/edit")
  public String edit(
      @PathVariable String rdate,
      EditForm editForm,
      Model model
  ) {
    Calendar calendarRecord = new Calendar();
    calendarRecord.setCdContent(editForm.getCdContent());
    calendarSVC.update(rdate, calendarRecord);

    model.addAttribute("form", editForm);
    return "redirect:/calendar/"+rdate;
  }

  // 운동기록 삭제
  @GetMapping("/{rdate}/del")
  public String del(@PathVariable String rdate) {
    calendarSVC.del(rdate);

    return "redirect:/calendar";
  }
}
