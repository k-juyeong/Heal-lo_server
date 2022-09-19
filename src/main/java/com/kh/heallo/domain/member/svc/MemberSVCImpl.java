package com.kh.heallo.domain.member.svc;

import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.dao.MemberDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberSVCImpl implements MemberSVC{

  private final MemberDAO memberDAO;

  /**
   * 가입
   *
   * @param member 가입정보
   * @return 가입건수
   */
  @Override
  public Long join(Member member) {
    return memberDAO.join(member);
  }

  /**
   * 조회 BY 회원번호
   *
   * @param memno 회원번호
   * @return 회원정보
   */
  @Override
  public Member findById(Long memno) {
    return memberDAO.findById(memno);
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
    memberDAO.update(memid,member);
  }

  /**
   * 탈퇴
   *
   * @param memid 비밀번호
   * @return
   */
  @Override
  public void del(String memid) {
    memberDAO.del(memid);
  }

  /**
   * 로그인
   * @param memid 아이디
   * @param mempw 비밀번호
   * @return  회원
   */
  @Override
  public Optional<Member> login(String memid, String mempw) {

    return memberDAO.login(memid,mempw);
  }
}
