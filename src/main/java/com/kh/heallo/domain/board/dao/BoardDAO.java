package com.kh.heallo.domain.board.dao;

import com.kh.heallo.domain.board.Board;

import java.util.List;
import java.util.Optional;

public interface BoardDAO {
  //게시글등록
  Long save(Board board);

  //게시글목록
  List<Board> findAll();

  //게시글조회
  Optional<Board> findByBoardId(Long boardId);

  //상품변경
  int update(Long BoardId, Board board);

  //상품삭제
  int deleteByBoardId(Long boardId);
}
