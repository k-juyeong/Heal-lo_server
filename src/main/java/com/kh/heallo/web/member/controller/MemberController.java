package com.kh.heallo.web.member.controller;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.svc.MemberSVC;
import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.web.member.dto.*;
import com.kh.heallo.web.session.LoginMember;
import com.kh.heallo.web.review.dto.ReviewDto;
import com.kh.heallo.web.session.Session;
import com.kh.heallo.web.utility.DtoModifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MemberSVC memberSVC;
  private final DtoModifier dtoModifier;

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

    if(bindingResult.hasErrors()){
    }

    //회원아이디 길이체크
    if(joinForm.getMemid().toLowerCase().trim().length() < 5 ||
            joinForm.getMemid().toUpperCase().trim().length() > 15){
      bindingResult.rejectValue("memid","chk.memid.length", "아이디 규칙을 지켜주세요");
    }

    //회원아이디 중복체크
    Boolean isExistId = memberSVC.dupChkOfMemid(joinForm.getMemid());
    if(isExistId){
      bindingResult.rejectValue("memid","dup.memid", "동일한 아이디가 존재합니다");
    }

    //회원비밀번호 길이체크
    if(joinForm.getMempw().toLowerCase().trim().length() < 8 ||
            joinForm.getMempw().toUpperCase().trim().length() > 16){
      bindingResult.rejectValue("mempw","dup.mempw", "비밀번호 규칙을 지켜주세요");
    }

    //회원비밀번호체크 길이체크
    if(joinForm.getMempwCk().toLowerCase().trim().length() < 8 ||
            joinForm.getMempwCk().toUpperCase().trim().length() > 16){
      bindingResult.rejectValue("mempwCk","dup.mempwCk", "비밀번호 규칙을 지켜주세요");
    }

    //회원전화번호 중복체크
    Boolean isExistTel = memberSVC.dupChkOfMemtel(joinForm.getMemtel());
    if(isExistTel){
      bindingResult.rejectValue("memtel","dup.memtel", "동일한 전화번호가 존재합니다");
    }

    //회원이메일 중복체크
    Boolean isExistEmail = memberSVC.dupChkOfMememail(joinForm.getMememail());
    if(isExistEmail){
      bindingResult.rejectValue("mememail","dup.mememail", "동일한 이메일이 존재합니다");
    }

    //회원닉네임 중복체크
    Boolean isExistNickname = memberSVC.dupChkOfMemnickname(joinForm.getMemnickname());
    if(isExistNickname){
      bindingResult.rejectValue("memnickname","dup.memnickname", "동일한 닉네임이 존재합니다");
    }

    if (bindingResult.hasErrors()){
      return "member/join";
    }

    Member member = new Member();
    member.setMemid(joinForm.getMemid());
    member.setMempw(joinForm.getMempw());
    member.setMempw(joinForm.getMempwCk());
    member.setMemtel(joinForm.getMemtel());
    member.setMemnickname(joinForm.getMemnickname());
    member.setMememail(joinForm.getMememail());
    member.setMemname(joinForm.getMemname());
    member.setMemcode(Member.MEMCODE_NORMAL);

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
                      @RequestParam(value = "requestURI", defaultValue = "/") String requestURI
  ){

    //기본 검증
    if (bindingResult.hasErrors()){
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
    if (findedMember.getMemstatus().equals("WITHDRAW")){
      bindingResult.reject("LoginForm.login","회원정보가 없습니다");
      return "login/login";
    }

    //세션에 회원정보 저장
    LoginMember loginMember = new LoginMember(findedMember.getMemno(), findedMember.getMemnickname());

    //request.getSession(false) : 세션정보가 있으면 가져오고 없으면 세션을 만듦
    HttpSession session = request.getSession(true);
    session.setAttribute(Session.LOGIN_MEMBER.name(), loginMember);

    if (requestURI.equals("/members/login")) {
      return "redirect:/";
    }
    return "redirect:"+requestURI;
  }

  //로그아웃
  @GetMapping("logout")
  public String logout(HttpServletRequest request){
    HttpSession session = request.getSession(false);
    if(session != null){
      session.invalidate();
    }
    return "redirect:/";  //초기화면으로 이동
  }

  //조회와 동시에 수정
  @GetMapping("/{id}/edit")
  public String findById(@PathVariable("id") Long memno, Model model,HttpServletRequest request){

    //회원번호 조회
    HttpSession session = request.getSession(false);
    if(session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
      memno = loginMember.getMemno();
    }

    Member findedMember = memberSVC.findBymemno(memno);

    EditForm editForm = new EditForm();
    editForm.setMemno(memno);
    editForm.setMemid(findedMember.getMemid());
    editForm.setMempw(findedMember.getMempw());
    editForm.setMemtel(findedMember.getMemtel());
    editForm.setMemnickname(findedMember.getMemnickname());
    editForm.setMememail(findedMember.getMememail());
    editForm.setMemname(findedMember.getMemname());

    model.addAttribute("form",editForm);
    model.addAttribute("status",findedMember.getMemcode());
    return "member/my_page";
  }

  //수정처리
  @PostMapping("/{id}/edit")
  public String update(@PathVariable("id") Long memno, @ModelAttribute("form") EditForm editForm,BindingResult bindingResult, HttpServletRequest request){

    //회원번호 조회
    HttpSession session = request.getSession(false);
    if(session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
      memno = loginMember.getMemno();
    }

    //회원이메일 중복체크
    Boolean isExistEmail = memberSVC.dupChkOfMememail(editForm.getMememail());
    if(editForm.getMememail().equals(memberSVC.findBymemno(memno).getMememail())){
    }else if(isExistEmail){
      bindingResult.rejectValue("mememail","dup.mememail", "동일한 이메일이 존재합니다");
    }

    //회원전화번호 중복체크
    Boolean isExistTel = memberSVC.dupChkOfMemtel(editForm.getMemtel());
    if(editForm.getMemtel().equals(memberSVC.findBymemno(memno).getMemtel())){
    }else if(isExistTel){
      bindingResult.rejectValue("memtel","dup.memtel", "동일한 전화번호가 존재합니다");
    }

    //회원비밀번호 길이체크
    if(editForm.getMempw().toLowerCase().trim().length() < 8 ||
            editForm.getMempw().toUpperCase().trim().length() > 16){
      bindingResult.rejectValue("mempw","dup.mempw", "비밀번호 규칙을 지켜주세요");
    }

    if (bindingResult.hasErrors()){
      return "member/my_page";
    }

    Member member = new Member();
    member.setMemno(memno);
    member.setMemname(editForm.getMemname());
    member.setMemnickname(editForm.getMemnickname());
    member.setMememail(editForm.getMememail());
    member.setMempw(editForm.getMempw());
    member.setMemtel(editForm.getMemtel());
    member.setMemudate(editForm.getMemudate());

    memberSVC.update(memno,member);

    return "redirect:/members/"+memno+"/edit";
  }

  //삭제(탈퇴)
  @GetMapping("/{id}/del")
  public String delete(@PathVariable("id") String memid) {
    Member member = memberSVC.findById(memid);
    memberSVC.del(memid);
    if (member.getMemcode().equals(Member.MEMCODE_SNS)) {

      return "redirect:https://nid.naver.com/internalToken/view/tokenList/pc/ko";
    }

    return "redirect:/members/logout";
  }

  //아이디 찾기 화면
  @GetMapping("/find_id")
  public String findIdPWForm(Model model){

    model.addAttribute("form", new FindIdForm());
    return "find_id_pw/find_id";
  }

  //아이디 찾기 처리
  @PostMapping("/find_id")
  public String findIdPW(@Valid @ModelAttribute("form") FindIdForm findIdForm, BindingResult bindingResult,  Model model) {

    FindIdForm findId = new FindIdForm();
    findId.setMemname(findIdForm.getMemname());
    findId.setMememail(findIdForm.getMememail());

//    if (bindingResult.hasErrors()){
//
//      return "find_id_pw/find_id";
//    }

    Member member = memberSVC.findId(findId.getMemname(), findId.getMememail());
    findId.setMemid(member.getMemid());

    //멤버 code 검증
    if (member.getMemid() != null) {
      Member member2 = memberSVC.findById(member.getMemid());
      if (member2.getMemcode().equals(Member.MEMCODE_SNS)) {
        bindingResult.reject("not.support","해당 이메일은 sns로그인을 이용해주세요");

        return "find_id_pw/find_id";
      }
    }

//    if (member == null){
//      bindingResult.reject("memberNotFound","입력한 정보로 아이디를 조회할 수 없습니다");
//
//      return "find_id_pw/find_id";
//    }

      model.addAttribute("form", findId);
      return "find_id_pw/success_find_id";

  }

  //비밀번호 재설정
  @GetMapping("/find_pw")
  public String findIdPWForm2(Model model){

    model.addAttribute("form",new FindIdForm());
    return "find_id_pw/find_pw";
  }

  @PostMapping("/find_pw")
  public String findPw(@Valid @ModelAttribute("form")FindPwForm findPwForm, BindingResult bindingResult, Model model){

    FindPwForm findPw = new FindPwForm();
    findPw.setMemid(findPwForm.getMemid());
    findPw.setMemname(findPwForm.getMemname());
    findPw.setMememail(findPwForm.getMememail());

//    if (bindingResult.hasErrors()){
//
//      return "find_id_pw/find_pw";
//    }

    Member findedMember = memberSVC.findPwCheck(findPw.getMemid(), findPw.getMemname(), findPw.getMememail());

    //멤버 code 검증
    if (findedMember.getMemno() != null) {
      Member member2 = memberSVC.findBymemno(findedMember.getMemno());
      if (member2.getMemcode().equals(Member.MEMCODE_SNS)) {
        bindingResult.reject("not.support","해당 이메일은 sns로그인을 이용해주세요");

        return "find_id_pw/find_pw";
      }
    }

//    if (findedMember == null){
//      bindingResult.reject("memberNotFound","입력한 정보로 아이디를 조회할 수 없습니다");
//
//      return "find_id_pw/find_pw";
//    }

    model.addAttribute("memno", findedMember.getMemno());
    return "find_id_pw/change_pw";
  }

  @PostMapping("/find_pw/{memno}")
  public String updatePw(@PathVariable("memno") Long memno, @ModelAttribute("form") FindPwForm findPwForm , BindingResult bindingResult){

    //회원비밀번호 길이체크
    if(findPwForm.getMempw().toLowerCase().trim().length() < 8 ||
            findPwForm.getMempw().toUpperCase().trim().length() > 16){
      bindingResult.rejectValue("mempw","dup.mempw", "비밀번호 규칙을 지켜주세요");
    }

    if (bindingResult.hasErrors()){
      return "find_id_pw/change_pw";
    }

    memberSVC.updatePw(memno,findPwForm.getMempw());
    return "redirect:/members/complete";
  }

  //비밀번호 재설정 완료 페이지
  @GetMapping("/complete")
  public String completeView(){

    return "find_id_pw/success_change_pw";
  }

  //마이페이지 활동 (게시글) 활동 이동 시 첫 페이지
  @GetMapping("/{id}/board")
  public String myActivityBoard(@PathVariable("id")Long memno, Model model,HttpServletRequest request){

    //회원번호 조회
    HttpSession session = request.getSession(false);
    if(session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
      memno = loginMember.getMemno();
    }

    EditForm editForm = new EditForm();
    editForm.setMemno(memno);

    List<Board> boards = memberSVC.findBoardByMemno(memno);
    List<Board> list = new ArrayList<>();

    boards.stream().forEach(board -> {
      Board board1 = new Board();
      BeanUtils.copyProperties(board,board1);
      list.add(board1);
    });

    Member findedMember = memberSVC.findBymemno(memno);

    model.addAttribute("form",editForm);
    model.addAttribute("list",list);
    model.addAttribute("status",findedMember.getMemcode());
    return "member/my_page_activity_board";
  }

  //마이페이지 활동 (댓글)
  @GetMapping("/{id}/reply")
  public String myActivityReply(@PathVariable("id")Long memno, Model model,HttpServletRequest request){
    //회원번호 조회
    HttpSession session = request.getSession(false);
    if(session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
      memno = loginMember.getMemno();
    }

    EditForm editForm = new EditForm();
    editForm.setMemno(memno);

    List<Reply> replies = memberSVC.findReplyByMemno(memno);
    List<Reply> list = new ArrayList<>();

    replies.stream().forEach(reply -> {
      Reply reply1 = new Reply();
      BeanUtils.copyProperties(reply,reply1);
      list.add(reply1);
    });

    Member findedMember = memberSVC.findBymemno(memno);

    model.addAttribute("form",editForm);
    model.addAttribute("list",list);
    model.addAttribute("status",findedMember.getMemcode());
    return "member/my_page_activity_reply";
  }

  //마이페이지 활동 (리뷰)
  @GetMapping("/{id}/review")
  public String myActivityReview(@PathVariable("id")Long memno, Long rvno,Model model,HttpServletRequest request){

    //회원번호 조회
    HttpSession session = request.getSession(false);
    if(session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
      memno = loginMember.getMemno();
    }

    EditForm editForm = new EditForm();
    editForm.setMemno(memno);

    List<Review> reviews = memberSVC.findReviewByMemno(memno,rvno);
    List<Review> list = new ArrayList<>();

    List<ReviewDto> reviewDtoList = reviews.stream().map(review -> {
      ReviewDto reviewDto = new ReviewDto();
      BeanUtils.copyProperties(review, reviewDto);

      return reviewDto;
    }).collect(Collectors.toList());


    reviews.stream().forEach(review->{
      Review review1 = new Review();
      BeanUtils.copyProperties(review,review1);
      list.add(review1);
    });

    Member findedMember = memberSVC.findBymemno(memno);

    model.addAttribute("form",editForm);
    model.addAttribute("list", reviewDtoList);
    model.addAttribute("status",findedMember.getMemcode());
    return "member/my_page_activity_review";
  }

}
