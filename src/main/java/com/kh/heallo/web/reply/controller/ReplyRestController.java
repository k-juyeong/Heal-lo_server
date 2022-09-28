package com.kh.heallo.web.reply.controller;

import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.reply.svc.ReplySVC;
import com.kh.heallo.web.member.session.LoginMember;
import com.kh.heallo.web.reply.dto.AddForm;
import com.kh.heallo.web.reply.dto.EditForm;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import com.kh.heallo.web.session.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyRestController {

  private final ReplySVC replySVC;
  // 댓글 목록 조회
  @GetMapping
  public ResponseEntity<ResponseMsg> all(Long bdno) {
    List<Reply> all = replySVC.all(bdno);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS)
        .setData("all", all);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 댓글 등록
  @PostMapping
  public ResponseEntity<ResponseMsg> save(
      AddForm addForm,
      HttpServletRequest request
  ) {

    // 회원번호 찾기
    Long memno = 0L;
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
      memno = loginMember.getMemno();
    }

    Reply reply = new Reply();
    reply.setRpComment(addForm.getRpComment());
    replySVC.save(memno, reply);

    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS);

    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 대댓글 등록
  @PostMapping("/plus")
  public ResponseEntity<ResponseMsg> savePlus(
      AddForm addForm,
      HttpServletRequest request
      ) {

    // 회원번호 찾기
    Long memno = 0L;
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
      memno = loginMember.getMemno();
    }

    Reply reply = new Reply();
    reply.setRpComment(addForm.getRpComment());
    replySVC.savePlusReply(memno, reply);

    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS);

    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 댓글 수정
  // 본인 댓글이면 수정 삭제 버튼 노출
  @PatchMapping("/{rpno}")
  public ResponseEntity<ResponseMsg> update(@PathVariable Long rpno, EditForm editForm) {
    Reply reply = new Reply();
    reply.setRpComment(editForm.getRpComment());

    replySVC.update(rpno, reply);

    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS);

    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 댓글 삭제
  @DeleteMapping("/{rpno}")
  public ResponseEntity<ResponseMsg> del(@PathVariable Long rpno) {
    replySVC.delete(rpno);

    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS);

    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

}
