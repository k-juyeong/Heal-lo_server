package com.kh.heallo.web.member.controller;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.svc.MemberSVC;
import com.kh.heallo.web.session.LoginMember;
import com.kh.heallo.web.session.Session;
import com.kh.heallo.web.sns.naver.NaverLoginDTO;
import com.kh.heallo.web.sns.naver.NaverLoginUtile;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import com.kh.heallo.web.sns.naver.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.netty.http.server.HttpServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberRestController {

  private final MemberSVC memberSVC;
  private final NaverLoginUtile naverLoginUtile;

  //네이버 인증 요청
  @GetMapping("/naver-join")
  public ResponseEntity<ResponseMsg> naverJoin() {
    String url = naverLoginUtile.createURL();

    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("redirect", url);

    return new ResponseEntity<>(responseMsg,HttpStatus.OK);
  }

  //네이버 로그인 처리
  @GetMapping("/naver-callback/redirect")
  public ResponseEntity<?> naverLogin(HttpServletRequest request, HttpServerResponse response, @RequestParam Map<String, String> resValue) {

    log.info("resValue {}", resValue);

    // code 를 받아오면 code 를 사용하여 access_token를 발급받는다.
    final NaverLoginDTO naverLoginDTO = naverLoginUtile.accessToken(resValue, "authorization_code");

    // access_token를 사용하여 사용자의 프로필 조회
    final UserInfo userInfo = naverLoginUtile.requestUserInfo(naverLoginDTO);

    Member member = new Member();
    member.setMemid(userInfo.getId().substring(0,10));
    member.setMempw("@@@@");
    member.setMemtel(userInfo.getMobile());
    member.setMemnickname(userInfo.getNickname());
    member.setMememail(userInfo.getEmail());
    member.setMemname(userInfo.getName());
    member.setMemcode(Member.MEMCODE_SNS);

    //해당 이메일이 첫 가입인 경우
    Member findedMember = memberSVC.findByEmail(userInfo.getEmail());
    LoginMember loginMember;
    if (findedMember == null) {

      //유저 닉네임 랜덤 부여
      while (true) {
        member.setMemnickname("user" + UUID.randomUUID().toString().substring(0, 5));
        if (!memberSVC.dupChkOfMemnickname(member.getMemnickname())) break;
      }

      Long memno = memberSVC.join(member);
      member.setMemno(memno);

      loginMember = new LoginMember(memno, member.getMemnickname());
    } else {

      loginMember = new LoginMember(findedMember.getMemno(), findedMember.getMemnickname());
      findedMember.setMemstatus(Member.MEMSTATUS_JOIN);
      memberSVC.updateStatus(findedMember.getMemno(), findedMember);
    }

    HttpSession session = request.getSession(true);
    session.setAttribute(Session.LOGIN_MEMBER.name(), loginMember);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  @GetMapping("/nickname-check")
  public ResponseEntity<ResponseMsg> nickCk(@RequestParam("nickname") String nickname){
    Boolean chk = memberSVC.dupChkOfMemnickname(nickname);
    ResponseMsg responseMsg = new ResponseMsg();
    if (chk) {
      responseMsg.createHeader(StatusCode.VALIDATION_ERROR);

      return new ResponseEntity<>(responseMsg, HttpStatus.BAD_REQUEST);
    }

    responseMsg.createHeader(StatusCode.SUCCESS);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }
}
