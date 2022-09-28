package com.kh.heallo.domain.reply.svc;

import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.reply.dao.ReplyDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplySVCImpl implements ReplySVC{

  private final ReplyDAO replyDAO;

  /**
   * 게시글에 해당하는 댓글 목록 조회
   *
   * @param bdno
   * @return 댓글 목록
   */
  @Override
  public List<Reply> all(Long bdno) {
    List<Reply> all = replyDAO.all(bdno);

    return all;
  }

  /**
   * 댓글 등록
   *
   * @param reply 등록할 댓글
   * @return 댓글 번호
   */
  @Override
  public Long save(Long memno, Reply reply) {

    return replyDAO.save(memno, reply);
  }

  /**
   * 대댓글 등록
   *
   * @param reply 등록할 대댓글
   * @return 댓글 번호
   */
  @Override
  public Long savePlusReply(Long memno, Reply reply) {

    return replyDAO.savePlusReply(memno, reply);
  }

  /**
   * 댓글 수정
   *
   * @param rpno  수정할 댓글 번호
   * @param reply 수정할 댓글 내용
   */
  @Override
  public void update(Long rpno, Reply reply) {
    replyDAO.update(rpno, reply);
  }

  /**
   * 댓글 삭제
   *
   * @param rpno 삭제할 댓글 번호
   */
  @Override
  public void delete(Long rpno) {
    replyDAO.delete(rpno);
  }
}
