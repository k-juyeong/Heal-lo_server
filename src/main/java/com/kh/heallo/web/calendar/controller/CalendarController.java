package com.kh.heallo.web.calendar.controller;

import com.kh.heallo.domain.calendar.Calendar;
import com.kh.heallo.domain.calendar.svc.CalendarSVC;
import com.kh.heallo.domain.uploadfile.svc.UploadFileSVC;
import com.kh.heallo.web.calendar.dto.AddForm;
import com.kh.heallo.web.calendar.dto.DayForm;
import com.kh.heallo.web.calendar.dto.EditForm;
import com.kh.heallo.web.session.LoginMember;
import com.kh.heallo.web.session.Session;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

  private final CalendarSVC calendarSVC;
  private final UploadFileSVC uploadFileSVC;

  // 달력 메인 (전체)
  @GetMapping
  public String calendar(
    HttpServletRequest request
  ) {

    // 로그인 여부
//    HttpSession session = request.getSession(false);
//    if (session != null) {
//      session.invalidate();
//    }

    return "calendar/calendarForm";
  }

  // 운동기록 등록화면
  @GetMapping("/{rdate}/add")
  public String addForm(
      @PathVariable("rdate") String cdRDate,
      Model model,
      HttpServletRequest request
  ) {

//    //회원 번호 조회
//    HttpSession session = request.getSession(false);
//    if (session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
//      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
//      Long memno = loginMember.getMemno();
//    }

    model.addAttribute("cdRDate", cdRDate);
    model.addAttribute("form", new AddForm());
    return "calendar/addForm";
  }

  // 운동기록 등록처리
  @PostMapping("/{rdate}/add")
  public String add(
    @ModelAttribute("form") AddForm addForm,
    BindingResult bindingResult,
    RedirectAttributes redirectAttributes,
    HttpServletRequest request
  ) throws IOException {
    // 기본 검증
    if (bindingResult.hasErrors()) {
      return "calendar/addForm";
    }

    // 오브젝트 검증
    // 첨부파일 & 내용 아무것도 없을 때

    // 회원번호
    Long memno = 0L;
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
      memno = loginMember.getMemno();
    }

    Calendar calendarRecord = new Calendar();
    String rdate = addForm.getCdRDate();
    calendarRecord.setMemno(memno);
    calendarRecord.setCdRDate(rdate);
    calendarRecord.setCdContent(addForm.getCdContent());


    calendarSVC.save(calendarRecord.getMemno(), rdate, calendarRecord);


    redirectAttributes.addAttribute("rdate", rdate);
    return "redirect:/calendar/{rdate}";
  }

  // 운동기록 조회
  @GetMapping("/{rdate}")
  public String findByDate(
      @PathVariable String rdate,
      Model model,
      HttpServletRequest request
  ) {

    // 회원번호 가져오기
    HttpSession session = request.getSession(false);
    LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
    Long memno = loginMember.getMemno();

    DayForm dayForm = new DayForm();
    Optional<Calendar> foundRecord = calendarSVC.findByDate(rdate, memno);
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
      Model model,
      HttpServletRequest request
  ) {

    // 회원번호 가져오기
    HttpSession session = request.getSession(false);
    LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
    Long memno = loginMember.getMemno();

    EditForm editForm = new EditForm();
    Optional<Calendar> foundRecord = calendarSVC.findByDate(rdate, memno);
    if (!foundRecord.isEmpty()) {
      BeanUtils.copyProperties(foundRecord.get(), editForm);
    }


    model.addAttribute("rdate", rdate);
    model.addAttribute("form", editForm);
    return "calendar/editForm";
  }

  // 운동기록 수정처리
  @PostMapping("/{rdate}/edit")
  public String edit(
      @PathVariable String rdate,
      @ModelAttribute("form") EditForm editForm,
      Model model,
      HttpServletRequest request
//      List<MultipartFile> imageFiles
  ) {

    // 회원번호
    HttpSession session = request.getSession(false);
    LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
    Long memno = loginMember.getMemno();

    Calendar calendarRecord = new Calendar();
    calendarRecord.setCdContent(editForm.getCdContent());

    calendarSVC.update(rdate, calendarRecord, memno);

    model.addAttribute("form", editForm);
    return "redirect:/calendar/"+rdate;
  }

  // 운동기록 삭제
  @GetMapping("/{rdate}/del")
  public String del(
      @PathVariable String rdate, HttpServletRequest request
  ) {
    // 회원번호
    HttpSession session = request.getSession(false);
    LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
    Long memno = loginMember.getMemno();

    calendarSVC.del(rdate, memno);

    return "redirect:/calendar";
  }
}
