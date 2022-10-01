package com.kh.heallo.domain.board.svc;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.board.dao.BbsFilterCondition;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface BoardSVC {

  //게시글등록
  Long save(Board board);
  Long save(Board board, List<MultipartFile> files);

  //게시글목록
  List<Board> findAll();

  List<Board> findAll(int startRec, int endRec);
  List<Board>  findAll(String category,int startRec, int endRec);
  List<Board>  findAll(BbsFilterCondition filterCondition);


  //게시글조회
  Optional<Board> findByBoardId(Long boardId);

  //게시글변경
  void update(Long BoardId, Board board);
  //파일추가
  void update(Long BoardId, Board board,List<MultipartFile> files);
  //파일삭제
  void update(Long BoardId, Board board,Long[] deletedFiles);
  //파일추가, 삭제
  void update(Long BoardId, Board board,List<MultipartFile> files, Long[] deletedFiles);




  //게시글삭제
  void deleteByBoardId(Long boardId);

  int totalCount();
  int totalCount(String bdcg);
  int totalCount(BbsFilterCondition filterCondition);
}

