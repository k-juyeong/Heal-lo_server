package com.kh.heallo.web.mail;

import com.kh.heallo.domain.member.svc.MailSVC;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Slf4j
public class MailRestController {

  private final MailSVC mailSVC;

  //이메일 인증
  @PostMapping("/mailConfirm")
  ResponseEntity<ResponseMsg> mailConfirm(@RequestBody Email email) throws Exception{

    String code = mailSVC.sendSimpleMessage(email.getEmail());
    log.info("인증코드 : " + code);

    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("code", code);

    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  @Data
  static class Email {
    private String email;
  }

}

