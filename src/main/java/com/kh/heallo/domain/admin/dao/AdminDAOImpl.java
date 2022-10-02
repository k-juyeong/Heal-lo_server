package com.kh.heallo.domain.admin.dao;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.review.Review;
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

  /**
   * 회원 계정 닉네임, 아이디 검색
   * @param memInfo 닉네임
   * @return
   */
  @Override
  public List<Member> memberListByIdOrNickname(String memInfo) {
    StringBuffer sql = new StringBuffer();
    sql.append("select MEMNO, MEMID, MEMNICKNAME, MEMCDATE ");
    sql.append("  from MEMBER ");
    sql.append(" where MEMNICKNAME like '%" + memInfo + "%' or ");
    sql.append("       MEMID like '%" + memInfo + "%' ");
    sql.append(" order by MEMNO asc ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Member.class), memInfo);
  }

  /**
   * 게시글 목록
   *
   * @return
   */
  @Override
  public List<Board> boardList() {
    // 페이징 해야 됨
    StringBuffer sql = new StringBuffer();
    sql.append("select t1.BDNO, t1.BDTITLE, t2.MEMNICKNAME, t1.BDCDATE ");
    sql.append("  from BOARD t1, MEMBER t2 ");
    sql.append(" where t1.MEMNO = t2.MEMNO ");
    sql.append(" order by t1.BDNO asc ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Board.class));
  }

  /**
   * 게시물 - 게시글 제목 검색
   *
   * @param title 제목
   * @return
   */
  @Override
  public List<Board> boardListByTitle(String title) {
    StringBuffer sql = new StringBuffer();
    sql.append("select t1.BDNO, t1.BDTITLE, t2.MEMNICKNAME, t1.BDCDATE ");
    sql.append("  from BOARD t1, MEMBER t2 ");
    sql.append(" where t1.MEMNO = t2.MEMNO ");
    sql.append("   and t1.TITLE like '%" + title + "%' ");
    sql.append(" order by t1.BDNO asc ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Board.class));
  }

  /**
   * 게시물 - 게시글 작성자(닉네임, 아이디) 검색
   * @param memInfo 닉네임, 아이디
   * @return
   */
  @Override
  public List<Board> boardListByIdOrNickname(String memInfo) {
    StringBuffer sql = new StringBuffer();
    sql.append("select t1.BDNO, t1.BDTITLE, t2.MEMNICKNAME, t1.BDCDATE ");
    sql.append("  from BOARD t1, MEMBER t2 ");
    sql.append(" where t1.MEMNO = t2.MEMNO ");
    sql.append("   and MEMNICKNAME like '%" + memInfo + "%' or ");
    sql.append("       MEMID like '%" + memInfo + "%' ");
    sql.append(" order by t1.BDNO asc ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Board.class));
  }

  /**
   * 게시물 - 댓글 목록
   *
   * @return
   */
  @Override
  public List<Reply> replyList() {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT t1.rpno, t1.rpcomment, t2.memnickname, t1.rpcdate ");
    sql.append("  FROM reply t1, member t2 ");
    sql.append(" WHERE t1.memno = t2.memno ");
    sql.append(" ORDER BY t1.rpno ASC ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Reply.class));
  }

  /**
   * 게시물 - 댓글 내용 검색
   *
   * @param rpContent 댓글 내용
   * @return
   */
  @Override
  public List<Reply> replyListByContent(String rpContent) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT t1.rpno, t1.rpcomment, t2.memnickname, t1.rpcdate ");
    sql.append("  FROM reply t1, member t2 ");
    sql.append(" where t1.MEMNO = t2.MEMNO ");
    sql.append("   and t1.rpcomment like '%" + rpContent + "%' ");
    sql.append(" order by t1.rpno asc ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Reply.class));
  }

  /**
   * 게시물 - 댓글 작성자(닉네임,아이디) 검색
   *
   * @param memInfo 닉네임,아이디
   * @return
   */
  @Override
  public List<Reply> replyListByIdOrNickname(String memInfo) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT t1.rpno, t1.rpcomment, t2.memnickname, t1.rpcdate ");
    sql.append("  FROM reply t1, member t2 ");
    sql.append(" where t1.MEMNO = t2.MEMNO ");
    sql.append("   and MEMNICKNAME like '%" + memInfo + "%' or ");
    sql.append("       MEMID like '%" + memInfo + "%' ");
    sql.append(" order by t1.rpno asc ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Reply.class));
  }

  /**
   * 게시물 - 리뷰 목록
   *
   * @return
   */
  @Override
  public List<Review> reviewList() {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT t1.rvno, t1.rvcontents, t2.memnickname, t1.fcno ");
    sql.append("  FROM review t1, member t2, facility t3 ");
    sql.append(" where t1.MEMNO = t2.MEMNO ");
    sql.append("   and t1.fcno = t3.fcno ");
    sql.append(" order by t1.rvno asc ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Review.class));
  }

  /**
   * 게시물 - 리뷰 내용 검색
   *
   * @param content 리뷰 내용
   * @return
   */
  @Override
  public List<Review> reviewListByContent(String content) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT t1.rvno, t1.rvcontents, t2.memnickname, t1.fcno ");
    sql.append("  FROM review t1, member t2, facility t3 ");
    sql.append(" where t1.MEMNO = t2.MEMNO ");
    sql.append("   and t1.fcno = t3.fcno ");
    sql.append("   and t1.rvcontents like '%" + content + "%' ");
    sql.append(" order by t1.rvno asc ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Review.class));
  }

  /**
   * 게시물 - 리뷰 운동시설 검색
   *
   * @param fcName 운동시설
   * @return
   */
  @Override
  public List<Review> reviewListByFacility(String fcName) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT t1.rvno, t1.rvcontents, t2.memnickname, t1.fcno ");
    sql.append("  FROM review t1, member t2, facility t3 ");
    sql.append(" where t1.MEMNO = t2.MEMNO ");
    sql.append("   and t1.fcno = t3.fcno ");
    sql.append("   and t3.fcname like '%" + fcName + "%' ");
    sql.append(" order by t1.rvno asc ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Review.class));
  }

  /**
   * 게시물 - 리뷰 작성자(닉네임) 검색
   *
   * @param memInfo 닉네임
   * @return
   */
  @Override
  public List<Review> reviewListByIdOrNickname(String memInfo) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT t1.rvno, t1.rvcontents, t2.memnickname, t1.fcno ");
    sql.append("  FROM review t1, member t2, facility t3 ");
    sql.append(" where t1.MEMNO = t2.MEMNO ");
    sql.append("   and t1.fcno = t3.fcno ");
    sql.append("   and t2.MEMNICKNAME like '%" + memInfo + "%' or ");
    sql.append("       t2.MEMID like '%" + memInfo + "%' ");
    sql.append(" order by t1.rvno asc ");

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Review.class));
  }


  /**
   * 운동시설 정보 수정
   *
   * @param facility 운동시설 정보
   */
  @Override
  public void updateFacility(Facility facility) {

  }
}

