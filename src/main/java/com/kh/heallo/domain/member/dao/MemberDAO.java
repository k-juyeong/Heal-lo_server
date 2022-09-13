package com.kh.heallo.domain.member.dao;

import com.kh.heallo.domain.member.Member;

public interface MemberDAO {

  /**
   * 가입
   * @param member 가입정보
   * @return  가입건수
   */
  int join(Member member);

  /**
   * 조회 BY 회원 비밀번호
   * @param mempw 회원 비밀번호
   * @return  회원정보
   */
  Member findByPw(String mempw);

  /**
   * 수정
   * @param mempw  아이디
   * @param member 수정할 정보
   * @return  수정건수
   */
  int update(String mempw, Member member);

  /**
   * 탈퇴
   * @param mempw 비밀번호
   * @return
   */
  int del(String mempw);

}


