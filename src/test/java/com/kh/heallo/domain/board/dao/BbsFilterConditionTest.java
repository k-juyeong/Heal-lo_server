package com.kh.heallo.domain.board.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@Slf4j
@SpringBootTest
class BbsFilterConditionTest {
@Autowired
private BoardDAO boardDAO;
  @Test
  void getCategory() {
    BbsFilterCondition filterCondition = new BbsFilterCondition(
        "BD001","A","제목");

    int cnt = boardDAO.totalCount(filterCondition);
    log.info("count={}", cnt);
  }

  @Test
  void getStartRec() {
  }

  @Test
  void getEndRec() {
  }

  @Test
  void getSearchType() {
  }

  @Test
  void getKeyword() {
  }

  @Test
  void testToString() {
  }
}