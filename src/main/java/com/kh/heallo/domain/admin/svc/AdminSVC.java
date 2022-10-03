package com.kh.heallo.domain.admin.svc;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.review.Review;

import java.util.List;

public interface AdminSVC {
  /**
   * 회원 계정 목록
   * @return
   */
  List<Member> memberList();

  /**
   * 회원 계정 닉네임,아이디 검색
   * @param memInfo 닉네임,아이디
   * @return
   */
  List<Member> memberListByIdOrNickname(String memInfo);

  /**
   * 게시글 목록
   * @return
   */
  List<Board> boardList();

  /**
   * 게시물 - 게시글 제목 검색
   * @param title 제목
   * @return
   */
  List<Board> boardListByTitle(String title);

  /**
   * 게시물 - 게시글 작성자(닉네임, 아이디) 검색
   * @param memInfo 닉네임, 아이디
   * @return
   */
  List<Board> boardListByIdOrNickname(String memInfo);


  /**
   * 게시물 - 댓글 목록
   * @return
   */
  List<Reply> replyList();

  /**
   * 게시물 - 댓글 내용 검색
   * @param content 댓글 내용
   * @return
   */
  List<Reply> replyListByContent(String content);


  /**
   * 게시물 - 댓글 작성자(닉네임,아이디) 검색
   * @param memInfo 닉네임,아이디
   * @return
   */
  List<Reply> replyListByIdOrNickname(String memInfo);

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
   * 게시물 - 리뷰 작성자(닉네임,아이디) 검색
   * @param memInfo 닉네임,아이디
   * @return
   */
  List<Review> reviewListByIdOrNickname(String memInfo);

  // 게시물 - 문의글 목록

  // 게시물 - 문의글 내용 검색
  // 게시물 - 문의글 작성자(닉네임) 검색
  // 게시물 - 문의글 작성자(아이디) 검색

  /**
   * 운동시설 정보 수정
   * @param facility 운동시설 정보
   */
  void updateFacility(Facility facility);

}
