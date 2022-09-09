package com.kh.heallo.domain.member.dao;

import com.kh.heallo.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements  MemberDAO{

  private final JdbcTemplate jdbcTemplate;

  @Override
  public int join(Member member) {
    return 0;
  }

  @Override
  public Member findByPw(String mempw) {
    return null;
  }

  @Override
  public int update(String mempw, Member member) {
    return 0;
  }

  @Override
  public int del(String mempw) {
    return 0;
  }
}
