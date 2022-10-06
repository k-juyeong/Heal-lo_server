package com.kh.heallo.domain.board.svc;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.board.dao.BbsFilterCondition;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@Slf4j
@SpringBootTest
class BoardSVCImplTest {
  @Autowired
private BoardSVC boardSVC;

  @Test
  void test(){

    BbsFilterCondition bbsFilterCondition = new BbsFilterCondition("BD001",1,10,"A","닉네임1");
    List<Board> boards = boardSVC.findAll(bbsFilterCondition);
    int i = boardSVC.totalCount(bbsFilterCondition);
    log.info("totalcount={}",i);

    Assertions.assertThat(boards.size()).isEqualTo(i);

  }
}