package com.kh.heallo.web.member.controller;

import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.svc.MemberSVC;
import com.kh.heallo.web.member.dto.EditForm;
import com.kh.heallo.web.member.dto.JoinForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
