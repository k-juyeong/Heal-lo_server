package com.kh.heallo.web.member.controller;

import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.svc.MemberSVC;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import com.kh.heallo.web.session.LoginMember;
import com.kh.heallo.web.session.Session;
import com.kh.heallo.web.snsjoin.naver.NaverLoginDTO;
import com.kh.heallo.web.snsjoin.naver.NaverLoginUtile;
import com.kh.heallo.web.snsjoin.naver.UserInfo;
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

    // 계정이 저장되있지 않다면 저장
    Member findedMember = memberSVC.findById(userInfo.getId());
    if (findedMember == null) {
      Member member = new Member();
      member.setMemid(userInfo.getId());
      member.setMempw("");
      member.setMemtel(userInfo.getMobile());
      member.setMemnickname(userInfo.getNickname());
      member.setMememail(userInfo.getEmail());
      member.setMemname(userInfo.getName());

      Long memno = memberSVC.join(member);
      member.setMemno(memno);
      findedMember = member;
    }

    //세션에 회원정보 저장
    LoginMember loginMember = new LoginMember(findedMember.getMemno(), findedMember.getMemnickname());

    //request.getSession(false) : 세션정보가 있으면 가져오고 없으면 세션을 만듦
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
