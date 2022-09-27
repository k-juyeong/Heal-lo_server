package com.kh.heallo.domain.admin.svc;

import com.kh.heallo.domain.member.Member;

import java.util.List;

public interface AdminSVC {
  /**
   * 회원 계정 목록
   * @return
   */
  List<Member> memberList();

  /**
   * 회원 계정 닉네임 검색
   * @param nickname 닉네임
   * @return
   */
  List<Member> memberListByNickname(String nickname);

  /**
   * 회원 계정 아이디 검색
   * @param id 아이디
   * @return
   */
  List<Member> memberListById(String id);
}
