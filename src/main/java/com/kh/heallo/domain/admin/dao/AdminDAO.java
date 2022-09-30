package com.kh.heallo.domain.admin.dao;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.review.Review;

import java.util.List;

public interface AdminDAO {

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
   * @param memId 아이디
   * @return
   */
  List<Member> memberListById(String memId);

  /**
   * 회원 계정 삭제
   * @param memno 회원 번호
   */
  void memberDel(Long memno);

  // 게시물 - 게시글 목록
  // 게시물 - 게시글 제목 검색

  /**
   * 게시물 - 게시글 작성자(닉네임) 검색
   * @param nickname 닉네임
   * @return
   */
  List<Board> boardListByNickname(String nickname);


//  /**
//   * 게시물 - 게시글 작성자(아이디) 검색
//   * @param memId 아이디
//   * @return
//   */
//  List<Board> boardListById(String memId);

  /**
   * 게시글 삭제
   * @param bdno 게시글 번호
   */
  void boardDel(Long bdno);

  /**
   * 게시물 - 댓글 목록
   * @return
   */
  List<Reply> replyList();

  /**
   * 게시물 - 댓글 내용 검색
   * @param rpContent 댓글 내용
   * @return
   */
  List<Reply> replyListByContent(String rpContent);


  /**
   * 게시물 - 댓글 작성자(닉네임) 검색
   * @param nickname 닉네임
   * @return
   */
  List<Reply> replyListByNickname(String nickname);


//  /**
//   * 게시물 - 댓글 작성자(아이디) 검색
//   * @param memId 아이디
//   * @return
//   */
//  List<Reply> replyListById(String memId);

  /**
   * 댓글 삭제
   * @param rpno 댓글 번호
   */
  void replyDel(Long rpno);

  /**
   * 게시물 - 리뷰 목록
   * @return
   */
  List<Review> reviewList();


  /**
   * 게시물 - 리뷰 내용 검색
   * @param content 리뷰 내용
   * @return
   */
  List<Review> reviewListByContent(String content);


  /**
   * 게시물 - 리뷰 운동시설 검색
   * @param fcName 운동시설
   * @return
   */
  List<Review> reviewListByFacility(String fcName);

  /**
   * 게시물 - 리뷰 작성자(닉네임) 검색
   * @param nickname 닉네임
   * @return
   */
  List<Review> reviewListByNickname(String nickname);

//  /**
//   * 게시물 - 리뷰 작성자(아이디) 검색
//   * @param memId 아이디
//   * @return
//   */
//  List<Review> reviewListById(String memId);

  /**
   * 리뷰 삭제
   * @param rvno 리뷰 번호
   */
  void reviewDel(Long rvno);

  // 게시물 - 문의글 목록
  // 게시물 - 문의글 내용 검색
  // 게시물 - 문의글 작성자(닉네임) 검색
  // 게시물 - 문의글 작성자(아이디) 검색


}
