package com.kh.heallo.domain.board.dao;

import com.kh.heallo.domain.board.Board;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class BoardDAOImplTest {


  @Autowired
  private BoardDAO boardDAO;

  @Test
  void findAll() {
    List<Board> all = boardDAO.findAll(1, 10);
    log.info(all.toString());
    for(Board board:all){
      log.info(board.toString());
    }
  }


  @Test
  void findAll2(){
    List<Board> all = boardDAO.findAll("정보공유", 1, 25);
    for(Board board:all){
      log.info(board.toString());
    }
  }

  @Test
  void find(){
    BbsFilterCondition bbsFilterCondition = new BbsFilterCondition("자유게시판",1,10,"TC","본문");
    List<Board> boards = boardDAO.findAll(bbsFilterCondition);
    for(Board board:boards){
      log.info(board.toString());
    }
  }

  @Test
  void test(){

    BbsFilterCondition bbsFilterCondition = new BbsFilterCondition("자유게시판",1,10,"TC","본문");
    List<Board> boards = boardDAO.findAll(bbsFilterCondition);
    int i = boardDAO.totalCount(bbsFilterCondition);
    log.info("totalcount={}",i);

    Assertions.assertThat(boards.size()).isEqualTo(i);

  }
}