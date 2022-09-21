package com.kh.heallo.domain.board.svc;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.board.svc.dao.BoardDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardSVCImpl implements BoardSVC{
  private final BoardDAO boardDAO;
  //등록
  @Override
  public Long save(Board board) {
    return boardDAO.save(board);
  }

  //목록
  @Override
  public List<Board> findAll() {
    return boardDAO.findAll();
  }

  //조회
  @Override
  public Optional<Board> findByBoardId(Long boardId) {
    return boardDAO.findByBoardId(boardId);
  }

  //수정
  @Override
  public int update(Long boardId, Board board) {
    return boardDAO.update(boardId, board);
  }

  //삭제
  @Override
  public int deleteByBoardId(Long boardId) {
    return boardDAO.deleteByBoardId(boardId);
  }

}
