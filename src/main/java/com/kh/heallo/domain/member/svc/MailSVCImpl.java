package com.kh.heallo.domain.member.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class MailSVCImpl implements MailSVC{

  @Autowired
  JavaMailSender emailsender;

  private String ePw; //인증번호

  @Override
  public boolean checkEPw(String pw) {

    return pw == ePw;
  }

  /**
   * 메일 내용 작성
   *
   * @param to
   * @return
   */
  @Override
  public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
    System.out.println("보내는 대상 : " + to);
    System.out.println("인증 번호 : " + ePw);

    MimeMessage message = emailsender.createMimeMessage();

    message.addRecipients(Message.RecipientType.TO, to);  //보내는 대상
    message.setSubject("Heal-lo 이메일 인증");   //제목

    String msgg= "";
    msgg += "<div style='margin:100px;'>";
    msgg += "<h1> 안녕하세요! </h1>";
    msgg += "<h1> 나만의 건강한 장소, 헬-로! 입니다.</h1>";
    msgg += "<br>";
    msgg += "<p>아래 코드를 인증칸에 입력해주세요.</p>";
    msgg += "<br>";
    msgg += "<div align='center' style='border:1px solid black; font-family:verdana';></div>";
    msgg += "<h3 style='color:blue;'>인증 코드입니다.</h3>";
    msgg += "<div style='font-size:130%;'>";
    msgg += "CODE : <strong>";
    msgg += ePw + "</strong></div><br/>"; //메일에 인증번호 넣기
    msgg += "</div>";
    message.setText(msgg, "utf-8","html");  //내용,charset 타입, subtype
    //보내는 사람의 이메일 주소,보내는 사람 이름
    message.setFrom(new InternetAddress("heallo01@naver.com","헬로"));

    return message;
  }

  /**
   * 랜덤 인증 코드 전송
   *
   * @return
   */
  @Override
  public String createKey() {
    StringBuffer key = new StringBuffer();
    Random rnd = new Random();

    for (int i = 0; i < 8; i++){
      int index = rnd.nextInt(3); //0~2까지 랜덤, rnd값에 따라서 아래 switch 문이 실행됨

      switch (index){
        case 0:
          key.append((char) ((int) (rnd.nextInt(26)) + 97));
          break;
        case 1:
          key.append((char) ((int) (rnd.nextInt(26)) + 65));
          break;
        case 2:
          key.append((rnd.nextInt(10)));
          break;
      }
    }
    return key.toString();
  }

  /**
   * 메일 발송
   *
   * @param to
   * @return
   */
  @Override
  public String sendSimpleMessage(String to) throws Exception{

    ePw = createKey();

    MimeMessage message = createMessage(to);
    try {
      emailsender.send(message);
    }catch (MailException es){
      es.printStackTrace();
      throw new IllegalArgumentException();
    }
    return ePw;
  }
}
