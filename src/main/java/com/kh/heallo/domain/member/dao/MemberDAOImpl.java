package com.kh.heallo.domain.member.dao;

import com.kh.heallo.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements  MemberDAO{

  private final JdbcTemplate jdbcTemplate;

  /**
   * 가입
   *
   * @param member 가입정보
   * @return 가입건수
   */
  @Override
  public Member join(Member member) {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into member ");
    sql.append(" values (member_memno_seq.nextval ,? ,? ,? ,? ,? ,? ,? ,sysdate ,sysdate ) ");

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(con -> {
      PreparedStatement preparedStatement = con.prepareStatement(sql.toString(), new String[]{"memno"});
      preparedStatement.setString(1, member.getMemid());
      preparedStatement.setString(2, member.getMempw());
      preparedStatement.setString(3, member.getMemtel());
      preparedStatement.setString(4, member.getMemnickname());
      preparedStatement.setString(5, member.getMememail());
      preparedStatement.setString(6, member.getMemname());
      preparedStatement.setString(7, member.getMemcode());

      return preparedStatement;
    }, keyHolder);

    Long memno = Long.valueOf(keyHolder.getKeys().get("memno").toString());

    member.setMemno(memno);
    return member;
  }

  /**
   * 조회 BY 회원 비밀번호
   * @param mempw 회원 비밀번호
   * @return  회원정보
   */
  @Override
  public Member findByPw(String mempw) {
    StringBuffer sql = new StringBuffer();

    sql.append("select memid,mempw,memtel,memnickname,mememail,memname ");
    sql.append("  from member ");
    sql.append(" where pw= ? ");

    Member findedMember = null;
    try{
      findedMember = jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(Member.class), mempw);
    }catch (DataAccessException e) {
      log.info("찾고자하는 회원이 없습니다={}", mempw);
    }
    return findedMember;
  }

  /**
   * 수정
   * @param mempw  아이디
   * @param member 수정할 정보
   * @return  수정건수
   */
  @Override
  public int update(String mempw, Member member) {
    int result = 0;
    StringBuffer sql = new StringBuffer();

    sql.append("update MEMBER ");
    sql.append("   set MEMPW = ?, ");
    sql.append("       MEMTEL = ?, ");
    sql.append("       MEMNICKNAME = ?, ");
    sql.append("       MEMEMAIL = ?, ");
    sql.append("       MEMNAME = ?, ");
    sql.append("       MEMUDATE = sysdate ");
    sql.append(" where MEMPW = ? ");

    result = jdbcTemplate.update(sql.toString(),member.getMempw(), member.getMemtel(),
            member.getMemnickname(), member.getMememail(), member.getMemname(),member.getMemudate(),member.getMempw());

    return result;
  }

  /**
   * 탈퇴
   * @param mempw 비밀번호
   * @return
   */
  @Override
  public int del(String mempw) {
    int result = 0;
    String sql = "delete from MEMBER where MEMPW= ? ";

    result = jdbcTemplate.update(sql, mempw);

    return result;
  }
}
