package com.kh.heallo.domain.member.svc;

import com.kh.heallo.domain.member.Member;

import java.util.Optional;

public interface MemberSVC {

  /**
   * 가입
   *
   * @param member 가입정보
   * @return 가입건수
   */
  Long join(Member member);

  /**
   * 조회 BY 회원번호
   * @param memno 회원번호
   * @return  회원정보
   */
  Member findById(Long memno);

  /**
   * 수정
   * @param memno  아이디
   * @param member 수정할 정보
   * @return  수정건수
   */
  void update(Long memno, Member member);

  /**
   * 탈퇴
   * @param memid 비밀번호
   * @return
   */
  void del(String memid);

  /**
   * 로그인
   * @param memid 아이디
   * @param mempw 비밀번호
   * @return  회원
   */
  Optional<Member> login(String memid, String mempw);
}
