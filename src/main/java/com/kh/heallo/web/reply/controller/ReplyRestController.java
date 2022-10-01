package com.kh.heallo.web.reply.controller;

import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.reply.svc.ReplySVC;
import com.kh.heallo.web.board.dto.DetailForm;
import com.kh.heallo.web.reply.dto.AddForm;
import com.kh.heallo.web.reply.dto.ReplyDto;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import com.kh.heallo.web.session.LoginMember;
import com.kh.heallo.web.session.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyRestController {

  private final ReplySVC replySVC;

  // 댓글 수
  @GetMapping("/count/{bdno}")
  public ResponseEntity<ResponseMsg> count(@PathVariable Long bdno) {
    int count = replySVC.count(bdno);

    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("count", count);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 댓글 목록 조회
  @GetMapping("/{bdno}")
  public ResponseEntity<ResponseMsg> all(@PathVariable Long bdno, HttpServletRequest request) {
    List<Reply> all = replySVC.all(bdno);

    HttpSession session = request.getSession(false);
    List<ReplyDto> replyList = new ArrayList<>();
    if (session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
      LoginMember member = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
      Long memno = member.getMemno();
      replyList = all.stream().map(reply -> {
        ReplyDto replyDto = new ReplyDto();
        BeanUtils.copyProperties(reply, replyDto);
        if (reply.getMemno() == memno) {
          replyDto.setLogin(true);
        }
        return replyDto;
      }).collect(Collectors.toList());

    }

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS)
        .setData("replyList", replyList);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 댓글 등록
  @PostMapping("/{bdno}")
  public ResponseEntity<ResponseMsg> save(
      @PathVariable Long bdno,
      @RequestBody DetailForm detailForm,
      BindingResult bindingResult,
      HttpServletRequest request
  ) {
    // 검증
    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
    }
    log.info("detailForm={}", detailForm.getRpComment());
    // 회원번호 찾기
    Long memno = 0L;
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
      memno = loginMember.getMemno();
    }

    // AddForm -> Reply 로 변환
    Reply reply = new Reply();
    reply.setRpComment(detailForm.getRpComment());
    replySVC.save(bdno, memno, reply);

    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS);

    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 대댓글 등록
  @PostMapping("/plus/{bdno}")
  public ResponseEntity<ResponseMsg> savePlus(
      @PathVariable Long bdno,
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
    replySVC.savePlusReply(bdno, memno, reply);

    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS);

    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 댓글 수정
  // 본인 댓글이면 수정 삭제 버튼 노출
  @PatchMapping("/{rpno}")
  public ResponseEntity<ResponseMsg> update(
      @PathVariable Long rpno, @RequestBody DetailForm detailForm
  ) {
    Reply reply = new Reply();
    reply.setRpComment(detailForm.getRpComment());

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
