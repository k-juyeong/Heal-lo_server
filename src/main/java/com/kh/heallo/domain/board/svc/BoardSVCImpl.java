package com.kh.heallo.domain.board.svc;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.board.dao.BbsFilterCondition;
import com.kh.heallo.domain.board.dao.BoardDAO;
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
  @Override
  public List<Board> findAll(int startRec, int endRec) {
    return boardDAO.findAll(startRec,endRec);
  }
  @Override
  public List<Board> findAll(String bdcg, int startRec, int endRec) {
    return boardDAO.findAll(bdcg, startRec,endRec);
  }

  @Override
  public List<Board> findAll(BbsFilterCondition filterCondition) {
    return boardDAO.findAll(filterCondition);

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


  @Override
  public int totalCount() {
    return boardDAO.totalCount();

  }

  @Override
  public int totalCount(String bdcg) {
     return boardDAO.totalCount(bdcg);

  }

  @Override
  public int totalCount(BbsFilterCondition filterCondition) {
    return boardDAO.totalCount(filterCondition);

  }


}
