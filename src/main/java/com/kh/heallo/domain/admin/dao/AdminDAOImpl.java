package com.kh.heallo.domain.admin.dao;

import com.kh.heallo.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AdminDAOImpl implements AdminDAO {

  private final JdbcTemplate jdbcTemplate;
  /**
   * 회원 계정 목록
   * @return
   */
  @Override
  public List<Member> memberList() {
    StringBuffer sql = new StringBuffer();
    sql.append("select MEMNO, MEMID, MEMNICKNAME, MEMCDATE ");
    sql.append("  from MEMBER ");
    sql.append(" order by MEMNO asc ");


    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Member.class));
  }

  @Override
  public List<Member> memberListByNickname(String nickname) {
    StringBuffer sql = new StringBuffer();
    sql.append("select MEMNO, MEMID, MEMNICKNAME, MEMCDATE ");
    sql.append("  from MEMBER ");
    sql.append(" where MEMNICKNAME like ? ");


    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Member.class), nickname);
  }

  @Override
  public List<Member> memberListById(String memId) {
    StringBuffer sql = new StringBuffer();
    sql.append("select MEMNO, MEMID, MEMNICKNAME, MEMCDATE ");
    sql.append("  from MEMBER ");
    sql.append(" where MEMID like ? ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Member.class), memId);
  }
}
