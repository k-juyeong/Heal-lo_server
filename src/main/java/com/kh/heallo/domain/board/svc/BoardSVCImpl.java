package com.kh.heallo.domain.board.svc;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.board.dao.BbsFilterCondition;
import com.kh.heallo.domain.board.dao.BoardDAO;
import com.kh.heallo.domain.uploadfile.AttachCode;
import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.domain.uploadfile.svc.UploadFileSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardSVCImpl implements BoardSVC{
  private final BoardDAO boardDAO;
  private final UploadFileSVC uploadFileSVC;
  //등록
  @Override
  public Long save(Board board) {
    return boardDAO.save(board);
  }

  @Override
  public Long save(Board board, List<MultipartFile> files) {
    Long bdno = boardDAO.save(board);
    uploadFileSVC.fileUpload(AttachCode.BD_CODE, bdno, files);
    return bdno;
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
  public void update(Long boardId, Board board) {
    boardDAO.update(boardId, board);
  }

  @Override
  public void update(Long BoardId, Board board, List<MultipartFile> files) {
    boardDAO.update(board.getBdno(), board);
    uploadFileSVC.fileUpload(AttachCode.CD_CODE, board.getBdno(), files);
  }

  @Override
  public void update(Long BoardId, Board board, Long[] deletedFiles) {
    boardDAO.update(board.getBdno(), board);
    uploadFileSVC.delete(deletedFiles);
  }
  @Override
  public void update(Long BoardId, Board board, List<MultipartFile> files, Long[] deletedFiles) {
    boardDAO.update(board.getBdno(), board);
    uploadFileSVC.fileUpload(AttachCode.CD_CODE, board.getBdno(), files);
    uploadFileSVC.delete(deletedFiles);
  }



  //삭제
  @Override
  public void deleteByBoardId(Long boardId) {


    // 첨부파일 있는지 확인
    List<FileData> foundImagesList = uploadFileSVC.findImages(AttachCode.BD_CODE, boardId);
    boardDAO.deleteByBoardId(boardId);

    // 로컬 파일 삭제
    if (foundImagesList != null) {
      Long[] foundImages = foundImagesList.stream()
          .map(image -> image.getUfno())
          .toArray(Long[]::new);
      for (Long ufno : foundImages) {
//        uploadFileSVC.delete(ufno);
        log.info("images={}", ufno);
      }
//      uploadFileSVC.delete(foundImages);
    } else {
      log.info("없음");
    }

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
