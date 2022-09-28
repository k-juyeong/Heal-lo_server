package com.kh.heallo.domain.board.svc;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.board.dao.BbsFilterCondition;

import java.util.List;
import java.util.Optional;

public interface BoardSVC {
  //게시글등록
  Long save(Board board);

  //게시글목록
  List<Board> findAll();

  List<Board> findAll(int startRec, int endRec);
  List<Board>  findAll(String category,int startRec, int endRec);
  List<Board>  findAll(BbsFilterCondition filterCondition);


  //게시글조회
  Optional<Board> findByBoardId(Long boardId);

  //상품변경
  int update(Long BoardId, Board board);

  //상품삭제
  int deleteByBoardId(Long boardId);

  int totalCount();
  int totalCount(String bdcg);
  int totalCount(BbsFilterCondition filterCondition);
}

