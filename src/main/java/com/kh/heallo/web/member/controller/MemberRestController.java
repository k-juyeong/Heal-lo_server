package com.kh.heallo.web.member.controller;

import com.kh.heallo.domain.member.dao.MemberDAO;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberRestController {

  private final MemberDAO memberDAO;

  @GetMapping("/nickname-check")
  public ResponseEntity<ResponseMsg> nickCk(@RequestParam("nickname") String nickname){
    Boolean chk = memberDAO.dupChkOfMemnickname(nickname);
    ResponseMsg responseMsg = new ResponseMsg();
    if (chk) {
      responseMsg.createHeader(StatusCode.VALIDATION_ERROR);

      return new ResponseEntity<>(responseMsg, HttpStatus.BAD_REQUEST);
    }

    responseMsg.createHeader(StatusCode.SUCCESS);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }
}
