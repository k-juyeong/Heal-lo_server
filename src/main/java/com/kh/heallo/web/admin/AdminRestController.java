package com.kh.heallo.web.admin;


import com.kh.heallo.domain.admin.svc.AdminSVC;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/manage")
@RequiredArgsConstructor
public class AdminRestController {

  private final AdminSVC adminSVC;

  // 회원 계정 전체 목록
  @GetMapping("/memberAll")
  public ResponseEntity<ResponseMsg> members() {
    List<Member> memberAll = adminSVC.memberList();

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS)
        .setData("member", memberAll);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 -
}
