package com.kh.heallo.domain.member.svc;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public interface MailSVC {

  boolean checkEPw(String pw);

  /**
   * 메일 내용 작성
   * @param to
   * @return
   */
  MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException;

  /**
   * 랜덤 인증 코드 전송
   * @return
   */
  String createKey();

  /**
   * 메일 발송
   * @param to
   * @return
   */
  String sendSimpleMessage(String to) throws Exception;
}
