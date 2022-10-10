package com.kh.heallo.domain.reply.dao;

import com.kh.heallo.domain.admin.dao.AdminDAO;
import com.kh.heallo.domain.reply.Reply;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class ReplyDAOImplTest {

  @Autowired
  private ReplyDAO replyDAO;
  private AdminDAO adminDAO;

  @Test
  @DisplayName("댓글 수")
  void count() {
    Long bdno = 12L;

    int count = replyDAO.count(bdno);
    log.info("댓글 수={}", count);
  }

  @Test
  @DisplayName("댓글 목록")
  void all() {
    Long bdno = 12L;

    List<Reply> replyAll = replyDAO.all(bdno);
    log.info("replyAll={}", replyAll.size());

//    replyAll.stream().forEach(reply -> {
//      log.info("reply={}", reply);
//    });
  }

  @Test
  @DisplayName("댓글 저장")
  void save() {
    Long bdno = 12L;
    Long memno = 12L;
    String status = "POST";

    Reply reply = new Reply();
    reply.setRpComment("공감해요");
    reply.setMemno(memno);
    reply.setBdno(bdno);
    reply.setRpStep(0L);
    reply.setRpDepth(0L);
    reply.setRpGroup(1L);
    reply.setRpStatus(status);

    replyDAO.save(bdno, memno, reply);
    List<Reply> replies = adminDAO.replyListByContent(reply.getRpComment());

    log.info("reply={}", replies.size());

  }

  @Test
  @DisplayName("대댓글 저장")
  void savePlusReply() {
    Long bdno = 12L;
    Long memno = 12L;
    String status = "POST";

    Reply reply = new Reply();
    reply.setRpComment("공감해요22");
    reply.setMemno(memno);
    reply.setBdno(bdno);
    reply.setRpStep(1L);
    reply.setRpDepth(1L);
    reply.setRpGroup(1L);
    reply.setRpStatus(status);

    replyDAO.savePlusReply(bdno, memno, reply);
    List<Reply> replies = adminDAO.replyListByContent(reply.getRpComment());

    log.info("reply={}", replies.size());
  }

  @Test
  @DisplayName("댓글 수정")
  void update() {
    Long rpno = 1L;

    Reply reply = new Reply();
    reply.setRpComment("공감 안해요");

    replyDAO.update(rpno, reply);
    List<Reply> replies = adminDAO.replyListByContent(reply.getRpComment());

    log.info("reply={}", replies.size());
  }

  @Test
  @DisplayName("댓글 삭제")
  void delete() {
    Long rpno = 1L;

    replyDAO.delete(rpno);

    List<Reply> replyList = adminDAO.replyListByContent("공감 안해요");

    Assertions.assertThat(replyList.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("대댓글 있을 시 상태 변경")
  void deleteState() {
    Long rpno = 1L;
    Long bdno = 12L;

    replyDAO.deleteState(rpno);
    List<Reply> all = replyDAO.all(bdno);
    all.stream().forEach(reply -> {
      if (reply.getRpno() == rpno) {
        Assertions.assertThat(reply.getRpStatus()).isEqualTo("DELETED");
      }
    });
  }

  @Test
  @DisplayName("상태 변경된 댓글 중 대댓글이 다 삭제되면 삭제")
  void deleteFinally() {
    // 대댓글 삭제 후 삭제되는지 확인
    replyDAO.deleteFinally();

    List<Reply> replyAll = adminDAO.replyList();
  }

  @Test
  @DisplayName("게시글 삭제 시 댓글 전체 삭제")
  void deleteAll() {
    Long bdno = 12L;

    replyDAO.deleteAll(bdno);
    List<Reply> all = replyDAO.all(bdno);

    Assertions.assertThat(all.size()).isEqualTo(0);
  }

}