package com.kh.heallo.domain.admin.svc;

import com.kh.heallo.domain.admin.dao.AdminDAO;
import com.kh.heallo.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminSVCImpl implements AdminSVC {

  private final AdminDAO adminDAO;

  /**
   * 회원 계정 목록
   * @return
   */
  @Override
  public List<Member> memberList() {
    return adminDAO.memberList();
  }

  @Override
  public List<Member> memberListByNickname(String nickname) {
    return adminDAO.memberListByNickname(nickname);
  }

  @Override
  public List<Member> memberListById(String memId) {
    return adminDAO.memberListById(memId);
  }
}
