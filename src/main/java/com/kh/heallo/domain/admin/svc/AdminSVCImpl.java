package com.kh.heallo.domain.admin.svc;

import com.kh.heallo.domain.admin.dao.AdminDAO;
import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.review.Review;
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

  /**
   * 회원 계정 닉네임,아이디 검색
   * @param memInfo 닉네임,아이디
   * @return
   */
  @Override
  public List<Member> memberListByIdOrNickname(String memInfo) {
    return adminDAO.memberListByIdOrNickname(memInfo);
  }

  /**
   * 게시글 목록
   *
   * @return
   */
  @Override
  public List<Board> boardList() {
    return adminDAO.boardList();
  }

  /**
   * 게시물 - 게시글 제목 검색
   *
   * @param title 제목
   * @return
   */
  @Override
  public List<Board> boardListByTitle(String title) {
    return adminDAO.boardListByTitle(title);
  }

  /**
   * 게시물 - 게시글 작성자(닉네임, 아이디) 검색
   *
   * @param memInfo 닉네임, 아이디
   * @return
   */
  @Override
  public List<Board> boardListByIdOrNickname(String memInfo) {
    return adminDAO.boardListByIdOrNickname(memInfo);
  }

  /**
   * 게시물 - 댓글 목록
   *
   * @return
   */
  @Override
  public List<Reply> replyList() {
    return adminDAO.replyList();
  }

  /**
   * 게시물 - 댓글 내용 검색
   *
   * @param content 댓글 내용
   * @return
   */
  @Override
  public List<Reply> replyListByContent(String content) {
    return adminDAO.replyListByContent(content);
  }

  /**
   * 게시물 - 댓글 작성자(닉네임,아이디) 검색
   *
   * @param memInfo 닉네임,아이디
   * @return
   */
  @Override
  public List<Reply> replyListByIdOrNickname(String memInfo) {
    return adminDAO.replyListByIdOrNickname(memInfo);
  }


  /**
   * 게시물 - 리뷰 목록
   *
   * @return
   */
  @Override
  public List<Review> reviewList() {
    return adminDAO.reviewList();
  }

  /**
   * 게시물 - 리뷰 내용 검색
   *
   * @param content 리뷰 내용
   * @return
   */
  @Override
  public List<Review> reviewListByContent(String content) {
    return adminDAO.reviewListByContent(content);
  }

  /**
   * 게시물 - 리뷰 운동시설 검색
   *
   * @param fcName 운동시설
   * @return
   */
  @Override
  public List<Review> reviewListByFacility(String fcName) {
    return adminDAO.reviewListByFacility(fcName);
  }

  /**
   * 게시물 - 리뷰 작성자(닉네임,아이디) 검색
   *
   * @param memInfo 닉네임,아이디
   * @return
   */
  @Override
  public List<Review> reviewListByIdOrNickname(String memInfo) {
    return adminDAO.reviewListByIdOrNickname(memInfo);
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
