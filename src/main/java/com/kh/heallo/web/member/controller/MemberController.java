package com.kh.heallo.web.member.controller;

import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.svc.MemberSVC;
import com.kh.heallo.web.member.dto.EditForm;
import com.kh.heallo.web.member.dto.JoinForm;
import com.kh.heallo.web.member.dto.LoginForm;
import com.kh.heallo.web.member.session.LoginMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MemberSVC memberSVC;

  //회원가입
  @GetMapping("/join")
  public String joinForm(){

    return "member/join";
  }

  //회원가입 처리
  @PostMapping("/join")
  public String join(JoinForm joinForm){
    log.info("joinForm={}",joinForm);

    Member member = new Member();
    member.setMemid(joinForm.getMemid());
    member.setMempw(joinForm.getMempw());
    member.setMemtel(joinForm.getMemtel());
    member.setMemnickname(joinForm.getMemnickname());
    member.setMememail(joinForm.getMememail());
    member.setMemname(joinForm.getMemname());
    member.setMemcode(joinForm.getMemcode());

    memberSVC.join(member);

    return "login/login";
  }

  //로그인 화면
  @GetMapping("/login")
  public String loginForm(@ModelAttribute("form") LoginForm loginForm){

    return "login/login";
  }

  //로그인 처리
  @PostMapping("/login")
  public String login(@Valid @ModelAttribute("form")LoginForm loginForm,
                      BindingResult bindingResult,
                      HttpServletRequest request,
                      @RequestParam(value = "requestURI",required = false,defaultValue = "/") String requestURI
  ){

    //기본 검증
    if (bindingResult.hasErrors()){
      log.info("bindingResult={}",bindingResult);
      return "login/login";
    }

    //회원유무
    Optional<Member> member = memberSVC.login(loginForm.getMemid(), loginForm.getMempw());

    if(member.isEmpty()){
      bindingResult.reject("LoginForm.login","회원정보가 없습니다");
      return "login/login";
    }

    //회원인경우
    Member findedMember = member.get();

    //세션에 회원정보 저장
    LoginMember loginMember = new LoginMember(findedMember.getMemid(), findedMember.getMemnickname());

    //request.getSession(false) : 세션정보가 있으면 가져오고 없으면 세션을 만듦
    HttpSession session = request.getSession(true);
    session.setAttribute("loginMember",loginMember);

    if (requestURI.equals("/")) {
      return "index";
    }
    return "redirect:"+requestURI;
  }

  //로그아웃
  @GetMapping("logout")
  public String logout(HttpServletRequest request){
    //request.getSession(false) : 세션정보가 있으면 가져오고 없으면 세션을 만들지 않음
    HttpSession session = request.getSession(false);
    if(session != null){
      session.invalidate();
    }
    return "index";  //초기화면으로 이동
  }

  //조회와 동시에 수정
  @GetMapping("/{id}")
  public String findById(@PathVariable("id") Long memno, Model model){

    Member findedMember = memberSVC.findById(memno);

    EditForm editForm = new EditForm();
    editForm.setMemid(findedMember.getMemid());
    editForm.setMempw(findedMember.getMempw());
    editForm.setMemtel(findedMember.getMemtel());
    editForm.setMemnickname(findedMember.getMemnickname());
    editForm.setMememail(findedMember.getMememail());
    editForm.setMemname(findedMember.getMemname());

    model.addAttribute("editForm",editForm);

    return "member/my_page";
  }

  //수정처리
  @PostMapping("/{id}/edit")
  public String update(@PathVariable("id") String memid, EditForm editForm){

    Member member = new Member();
    member.setMempw(editForm.getMempw());
    member.setMemtel(editForm.getMemtel());
    member.setMemnickname(editForm.getMemnickname());
    member.setMememail(editForm.getMememail());
    member.setMemname(editForm.getMemname());

    memberSVC.update(memid,member);

    return "member/my_page";
  }

  //삭제(탈퇴)
  @GetMapping("/{id}/del")
  public String delete(@PathVariable("id") String memid) {

    memberSVC.del(memid);
    return "index";
  }
}
