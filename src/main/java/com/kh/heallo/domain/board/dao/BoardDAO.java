package com.kh.heallo.domain.board.dao;

import com.kh.heallo.domain.board.Board;

import java.util.List;
import java.util.Optional;

public interface BoardDAO {
  //게시글등록
  Long save(Board board);

  //게시글목록
  List<Board> findAll();
  List<Board> findAll(int startRec, int endRec);
  List<Board> findAll(String bdcg, int startRec, int endRec);
  //검색
  List<Board>  findAll(BbsFilterCondition filterCondition);


  //게시글조회
  Optional<Board> findByBoardId(Long boardId);

  //게시글변경
  int update(Long BoardId, Board board);

  //게시글삭제
  int deleteByBoardId(Long boardId);



  //게시글 조건검색 수
  int totalCount();
  int totalCount(String bdcg);
  int totalCount(BbsFilterCondition filterCondition);


//조회수 증가
  int increaseViewCount(Long boardId);


}
