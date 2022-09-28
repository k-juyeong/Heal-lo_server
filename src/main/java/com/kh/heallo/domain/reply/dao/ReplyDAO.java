package com.kh.heallo.domain.reply.dao;

import com.kh.heallo.domain.reply.Reply;

import java.util.List;

public interface ReplyDAO {

  /**
   * 게시글에 해당하는 댓글 목록 조회
   * @return 댓글 목록
   */
  List<Reply> all(Long bdno);

  /**
   * 댓글 등록
   * @param reply 등록할 댓글
   * @return 댓글 번호
   */
  // 매개변수에 멤버 번호 추가
  Long save(Reply reply);

  /**
   * 대댓글 등록
   * @param reply 등록할 대댓글
   * @return 댓글 번호
   */
  // 매개변수에 멤버 번호 추가
  Long savePlusReply(Reply reply);

  /**
   * 댓글 수정
   * @param rpno 수정할 댓글 번호
   * @param reply 수정할 댓글 내용
   */
  void update(Long rpno, Reply reply);

  /**
   * 댓글 삭제
   * @param rpno 삭제할 댓글 번호
   */
  void delete(Long rpno);
}
