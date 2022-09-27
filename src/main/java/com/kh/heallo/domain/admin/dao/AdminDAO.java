package com.kh.heallo.domain.admin.dao;

import com.kh.heallo.domain.member.Member;

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

  // 게시물 - 게시글 목록
  // 게시물 - 게시글 제목 검색
  // 게시물 - 게시글 작성자(닉네임) 검색
  // 게시물 - 게시글 작성자(아이디) 검색
  // 게시물 - 댓글 목록
  // 게시물 - 댓글 내용 검색
  // 게시물 - 댓글 작성자(닉네임) 검색
  // 게시물 - 댓글 작성자(아이디) 검색
  // 게시물 - 리뷰 목록
  // 게시물 - 리뷰 내용 검색
  // 게시물 - 리뷰 운동시설 검색
  // 게시물 - 리뷰 작성자(닉네임) 검색
  // 게시물 - 리뷰 작성자(아이디) 검색
  // 게시물 - 문의글 목록
  // 게시물 - 문의글 내용 검색
  // 게시물 - 문의글 작성자(닉네임) 검색
  // 게시물 - 문의글 작성자(아이디) 검색


}
