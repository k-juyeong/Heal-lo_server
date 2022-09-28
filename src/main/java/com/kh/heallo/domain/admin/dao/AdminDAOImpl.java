package com.kh.heallo.domain.admin.dao;

import com.kh.heallo.domain.board.Board;
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

  /**
   * 게시물 - 게시글 작성자(닉네임) 검색
   * @param nickname 닉네임
   * @return
   */
  @Override
  public List<Board> boardListByNickname(String nickname) {


    return null;
  }

  /**
   * 게시물 - 게시글 작성자(아이디) 검색
   *
   * @param memId 아이디
   * @return
   */
  @Override
  public List<Board> boardListById(String memId) {
    return null;
  }

  /**
   * 게시물 - 댓글 목록
   *
   * @return
   */
  @Override
  public List<Reply> replyList() {
    return null;
  }

  /**
   * 게시물 - 댓글 내용 검색
   *
   * @param rpContent 댓글 내용
   * @return
   */
  @Override
  public List<Reply> replyListByContent(String rpContent) {
    return null;
  }

  /**
   * 게시물 - 댓글 작성자(닉네임) 검색
   *
   * @param nickname 닉네임
   * @return
   */
  @Override
  public List<Reply> replyListByNickname(String nickname) {
    return null;
  }

  /**
   * 게시물 - 댓글 작성자(아이디) 검색
   *
   * @param memId 아이디
   * @return
   */
  @Override
  public List<Reply> replyListById(String memId) {
    return null;
  }

  /**
   * 게시물 - 리뷰 목록
   *
   * @return
   */
  @Override
  public List<Review> reviewList() {
    return null;
  }

  /**
   * 게시물 - 리뷰 내용 검색
   *
   * @param content 리뷰 내용
   * @return
   */
  @Override
  public List<Review> reviewListByContent(String content) {
    return null;
  }

  /**
   * 게시물 - 리뷰 운동시설 검색
   *
   * @param fcName 운동시설
   * @return
   */
  @Override
  public List<Review> reviewListByFacility(String fcName) {
    return null;
  }

  /**
   * 게시물 - 리뷰 작성자(닉네임) 검색
   *
   * @param nickname 닉네임
   * @return
   */
  @Override
  public List<Review> reviewListByNickname(String nickname) {
    return null;
  }

  /**
   * 게시물 - 리뷰 작성자(아이디) 검색
   *
   * @param memId 아이디
   * @return
   */
  @Override
  public List<Review> reviewListById(String memId) {
    return null;
  }
}
