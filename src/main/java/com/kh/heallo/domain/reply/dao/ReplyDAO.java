package com.kh.heallo.domain.reply.dao;

import com.kh.heallo.domain.reply.Reply;

import java.util.List;

public interface ReplyDAO {

  /**
   * 댓글 수
   * @param bdno 게시글 번호
   * @return
   */
  int count(Long bdno);

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
  Long save(Long bdno, Long memno, Reply reply);

  /**
   * 대댓글 등록
   * 대댓글 등록
   * @param reply 등록할 대댓글
   * @return 댓글 번호
   */
  Long savePlusReply(Long bdno, Long memno, Reply reply);

  /**
   * 댓글 수정
   * @param rpno 수정할 댓글 번호
   * @param reply 수정할 댓글 내용
   */
  void update(Long rpno, Reply reply);

  /**
   * 대댓글이 있는 경우 상태만 변경
   * @param rpno 삭제할 댓글 번호
   */
  void deleteState(Long rpno);

  /**
   * 대댓글이 없는 경우 댓글 삭제
   * @param rpno 삭제할 댓글 번호
   */
  void delete(Long rpno);

  /**
   * 'DELETED' 상태의 댓글의 대댓글이 모두 삭제된 경우
   * 해당 댓글도 삭제
   * @param rpno 삭제할 댓글 번호
   */
  void deleteFinally(Long rpno);

  /**
   * 게시글 삭제할 경우 해당 게시글의 모든 댓글 삭제
   * @param bdno
   */
  void deleteAll(Long bdno);

  /**
   * 대댓글의 경우 step을 한단계 더함
   * @param rpno 해당 대댓글 번호
   */
  void reReplyStep(Long rpno);

  /**
   * 대댓글의 대댓글 경우 그 이후 rpStep 번호 수정 (+1)
   * @param rpno 대댓글 번호 (rpDepth 사용)
   */
  void updatedStep(Long rpGroup,Long rpDepth, Long rpno);
}
