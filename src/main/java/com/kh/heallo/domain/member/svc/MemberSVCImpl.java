package com.kh.heallo.domain.member.svc;

import com.kh.heallo.domain.member.Member;

public class MemberSVCImpl implements MemberSVC{

  /**
   * 가입
   *
   * @param member 가입정보
   * @return 가입건수
   */
  @Override
  public Long join(Member member) {
    return null;
  }

  /**
   * 조회 BY 회원 비밀번호
   *
   * @param memno 회원 비밀번호
   * @return 회원정보
   */
  @Override
  public Member findById(Long memno) {
    return null;
  }

  /**
   * 수정
   *
   * @param memid  아이디
   * @param member 수정할 정보
   * @return 수정건수
   */
  @Override
  public void update(String memid, Member member) {
  }

  /**
   * 탈퇴
   *
   * @param memid 비밀번호
   * @return
   */
  @Override
  public void del(String memid) {

  }
}
