package com.kh.heallo.web.member.controller;

import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.svc.MemberSVC;
import com.kh.heallo.web.member.dto.EditForm;
import com.kh.heallo.web.member.dto.FindIdPwForm;
import com.kh.heallo.web.member.dto.JoinForm;
import com.kh.heallo.web.member.dto.LoginForm;
import com.kh.heallo.web.member.session.LoginMember;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.session.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
  public String joinForm(Model model){
    model.addAttribute("form", new JoinForm());
    return "member/join";
  }

  //회원가입 처리
  @PostMapping("/join")
  public String join(@Valid @ModelAttribute("form")
                     JoinForm joinForm,
                     BindingResult bindingResult){
    log.info("joinForm={}",joinForm);

    if(bindingResult.hasErrors()){
      log.info("errors={}",bindingResult);
      return "member/join";
    }

    if(joinForm.getMemid().toLowerCase().trim().length() <= 5 &&
            joinForm.getMemid().toUpperCase().trim().length() >= 15){
      bindingResult.rejectValue("memid","chk.memid.length", "아이디 규칙을 지켜주세요");
      return "member/join";
    }

    Member member = new Member();
    member.setMemid(joinForm.getMemid());
    member.setMempw(joinForm.getMempw());
    member.setMemtel(joinForm.getMemtel());
    member.setMemnickname(joinForm.getMemnickname());
    member.setMememail(joinForm.getMememail());
    member.setMemname(joinForm.getMemname());

    memberSVC.join(member);

    return "redirect:/members/login";
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

    log.info("member, {} " , loginForm);

    //회원유무
    Optional<Member> member = memberSVC.login(loginForm.getMemid(), loginForm.getMempw());

    if(member.isEmpty()){
      bindingResult.reject("LoginForm.login","회원정보가 없습니다");
      return "login/login";
    }

    //회원인경우
    Member findedMember = member.get();

    //세션에 회원정보 저장
    LoginMember loginMember = new LoginMember(findedMember.getMemno(), findedMember.getMemnickname());

    //request.getSession(false) : 세션정보가 있으면 가져오고 없으면 세션을 만듦
    HttpSession session = request.getSession(true);
    session.setAttribute(Session.LOGIN_MEMBER.name(), loginMember);

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
    return "redirect:/";  //초기화면으로 이동
  }

  //조회와 동시에 수정
  @GetMapping("/{id}/edit")
  public String findById(@PathVariable("id") Long memno, Model model,HttpServletRequest request){

    //회원 조회
    if (request.getAttribute(Session.NOT_LOGIN.name()) != null) {
      ResponseEntity<ResponseMsg> notLoginResponseEntity = (ResponseEntity) request.getAttribute(Session.NOT_LOGIN.name());
      return String.valueOf(notLoginResponseEntity);
    }

    //회원 조회 성공
    HttpSession session = request.getSession();
    LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());

    Member findedMember = memberSVC.findById(loginMember.getMemno());

    EditForm editForm = new EditForm();
    editForm.setMemno(loginMember.getMemno());
    editForm.setMemid(findedMember.getMemid());
    editForm.setMempw(findedMember.getMempw());
    editForm.setMemtel(findedMember.getMemtel());
    editForm.setMemnickname(findedMember.getMemnickname());
    editForm.setMememail(findedMember.getMememail());
    editForm.setMemname(findedMember.getMemname());

    log.info("loginMember.getMemno()={}",loginMember.getMemno());
    log.info("editForm={}",editForm);

    model.addAttribute("editForm",editForm);

    return "member/my_page";
  }

  //수정처리
  @PostMapping("/{id}/edit")
  public String update(@PathVariable("id") Long memno, EditForm editForm, HttpServletRequest request){

    //회원 조회
    if (request.getAttribute(Session.NOT_LOGIN.name()) != null) {
      ResponseEntity<ResponseMsg> notLoginResponseEntity = (ResponseEntity) request.getAttribute(Session.NOT_LOGIN.name());
      return String.valueOf(notLoginResponseEntity);
    }

    //회원 조회 성공
    HttpSession session = request.getSession();
    LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());


    Member member = new Member();
    member.setMemno(loginMember.getMemno());
    member.setMemname(editForm.getMemname());
    member.setMemnickname(editForm.getMemnickname());
    member.setMememail(editForm.getMememail());
    member.setMempw(editForm.getMempw());
    member.setMemtel(editForm.getMemtel());
    member.setMemudate(editForm.getMemudate());

    memberSVC.update(loginMember.getMemno(),member);

    log.info("editForm={}",editForm);
    log.info("member={}",member);

    return "redirect:/members/"+loginMember.getMemno()+"/edit";
  }

  //삭제(탈퇴)
  @GetMapping("/{id}/del")
  public String delete(@PathVariable("id") String memid) {

    memberSVC.del(memid);

    log.info("memid={}",memid);
    return "redirect:/";
  }

  //아이디 찾기 화면
  @GetMapping("/find_id_pw")
  public String findIdPWForm(){

    return "find_id_pw/find_id_pw";
  }

  //아이디 찾기 처리
  @PostMapping("/find_id_pw")
  public String findIdPW(@ModelAttribute("form") FindIdPwForm findIdPwForm, Model model){

    FindIdPwForm findId = new FindIdPwForm();
    findId.setMemname(findIdPwForm.getMemname());
    findId.setMememail(findIdPwForm.getMememail());

    Member id = memberSVC.findId(findId.getMemname(), findId.getMememail());
    findId.setMemid(id.getMemid());

    log.info("findId={}", findId);
    model.addAttribute("form", findId);
    return "find_id_pw/success_find_id";
  }

  //비밀번호 변경 화면
  @GetMapping("/find_id_pw/set_pw")
  public String setPwForm(){

    return "find_id_pw/change_pw";
  }

  //마이페이지 활동 (게시글) 활동 이동 시 첫 페이지
  @GetMapping("/{id}/board")
  public String myActivityBoard(@PathVariable("id")Long memno){

    return "member/my_page_activity_board";
  }

  //마이페이지 활동 (댓글)
  @GetMapping("/{id}/reply")
  public String myActivityReply(){

    return "member/my_page_activity_reply";
  }

  //마이페이지 활동 (리뷰)
  @GetMapping("/{id}/review")
  public String myActivityReview(@PathVariable("id")Long memno, Long rvno){

    //List<Review> reviews = memberSVC.findReviewByMemno(memno,rvno);

    return "member/my_page_activity_review";
  }

}
