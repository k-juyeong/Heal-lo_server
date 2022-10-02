package com.kh.heallo.web.admin;


import com.kh.heallo.domain.admin.svc.AdminSVC;
import com.kh.heallo.domain.common.paging.FindCriteria;
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
  private FindCriteria fc;

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

  // 회원 닉네임, 아이디 검색
  @GetMapping("/member/{memInfo}")
  public ResponseEntity<ResponseMsg> memberIdMemInfo(@PathVariable String memInfo) {
    List<Member> memberList = adminSVC.memberListByIdOrNickname(memInfo);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("memberList", memberList);
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


  // 게시물 - 게시글 목록

  // 게시물 - 게시글 제목 검색
  // 게시물 - 게시글 작성자 검색

  // 게시물 - 댓글 목록
  // 게시물 - 댓글 내용 검색
  // 게시물 - 댓글 작성자 검색

  // 게시물 - 리뷰 목록
  // 게시물 - 리뷰 내용 검색
  // 게시물 - 리뷰 작성자 검색

  // 게시물 - 문의글 목록
  // 게시물 - 문의글 제목 검색
  // 게시물 - 문의글 작성자 검색

  // 운동시설 정보 수정
}

