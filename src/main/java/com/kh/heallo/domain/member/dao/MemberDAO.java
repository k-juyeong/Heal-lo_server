package com.kh.heallo.domain.member.dao;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.review.Review;

import java.util.List;
import java.util.Optional;

public interface MemberDAO {

  /**
   * 가입
   *
   * @param member 가입정보
   * @return 가입건수
   */
  Long join(Member member);

  /**
   * 조회 BY 회원번호
   * @param memno 회원번호
   * @return  회원정보
   */
  Member findById(Long memno);

  /**
   * 수정
   * @param memno  아이디
   * @param member 수정할 정보
   * @return  수정건수
   */
  void update(Long memno, Member member);

  /**
   * 탈퇴
   * @param memid 비밀번호
   * @return
   */
  void del(String memid);

  /**
   * 로그인
   * @param memid 아이디
   * @param mempw 비밀번호
   * @return  회원
   */
  Optional<Member> login(String memid, String mempw);

  /**
   * 아이디 찾기
   * @param memname   이름
   * @param mememail  이메일
   * @return          아이디
   */
  Member findId(String memname, String mememail);

  /**
   * 비밀번호 찾기
   *
   * @param memid    아이디
   * @param memname  이름
   * @param mememail 이메일
   * @return
   */
  Member findPw(String memid,String memname, String mememail);

  /**
   * 로그인 계정 작성 게시글 조회
   * @param memno 회원번호
   * @return
   */
  List<Board> findBoardByMemno(Long memno);

  /**
   * 로그인 계정 작성 댓글 조회
   * @param memno  회원번호
   * @return
   */
  List<Reply> findReplyByMemno(Long memno);

  /**
   * 로그인 계정 작성 리뷰 조회
   * @param memno   회원번호
   * @param rvno    리뷰번호
   * @return        리뷰내용
   */
  List<Review> findReviewByMemno(Long memno, Long rvno);

  /**
   * 아이디 중복체크
   * @param memid 아이디
   * @return 존재하면 true
   */
  Boolean dupChkOfMemid(String memid);

  /**
   * 전화번호 중복체크
   * @param memtel 아이디
   * @return 존재하면 true
   */
  Boolean dupChkOfMemtel(String memtel);

  /**
   * 이메일 중복체크
   * @param mememail 아이디
   * @return 존재하면 true
   */
  Boolean dupChkOfMememail(String mememail);

  /**
   * 닉네임 중복체크
   * @param memnickname 닉네임
   * @return 존재하면 true
   */
  Boolean dupChkOfMemnickname(String memnickname);
}


