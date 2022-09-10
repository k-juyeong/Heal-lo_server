package com.kh.heallo.domain.member.dao;

import com.kh.heallo.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements  MemberDAO{

  private final JdbcTemplate jdbcTemplate;

  /**
   * 가입
   * @param member 가입정보
   * @return  가입건수
   */
  @Override
  public int join(Member member) {
    int result = 0;
    StringBuffer sql = new StringBuffer();
    sql.append("insert into member" );
    sql.append(" values (? ,? ,? ,? ,? ,? ,? ,? ,? ,? ) ");

    result = jdbcTemplate.update(
      sql.toString(), member.getMemno(), member.getMemid(), member.getMempw(), member.getMemtel(),
      member.getMemninkname(), member.getMememail(), member.getMemname(), member.getMemcode(), member.getMemcdate(), member.getMemudate());

    return result;
  }

  /**
   * 신규 회원아이디(내부관리용) 생성
   * @return 회원아이디
   */
  public Long generateMemno(){
    String sql = "select member_memno_seq.nextval from dual ";
    Long Memno = jdbcTemplate.queryForObject(sql, Long.class);
    return Memno;
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
    sql.append(" where pw=' ? ' ");

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
    
    return 0;
  }

  /**
   * 탈퇴
   * @param mempw 비밀번호
   * @return
   */
  @Override
  public int del(String mempw) {

    return 0;
  }
}
