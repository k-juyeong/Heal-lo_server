package com.kh.heallo.web.admin;


import com.kh.heallo.domain.admin.svc.AdminSVC;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.svc.MemberSVC;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {

  private final AdminSVC adminSVC;
  private final MemberSVC memberSVC;

  // 회원 계정 전체 목록
  @GetMapping("/memberAll")
  public ResponseEntity<ResponseMsg> members() {
    List<Member> memberAll = adminSVC.memberList();

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS)
        .setData("memberAll", memberAll);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 회원 계정 삭제
  @DeleteMapping("/member/{memid}")
  public ResponseEntity<ResponseMsg> memberDel(@PathVariable String memid){
    memberSVC.del(memid);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }


  // 게시물 -
}
