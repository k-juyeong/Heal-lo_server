package com.kh.heallo.domain.calendar.svc;

import com.kh.heallo.domain.calendar.Calendar;
import com.kh.heallo.domain.calendar.dao.CalendarDAO;
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
public class CalendarSVCImpl implements CalendarSVC{
  private final CalendarDAO calendarDAO;
  private final UploadFileSVC uploadFileSVC;


  /**
   * 운동기록 등록
   *
   * @param date     운동기록 날짜
   * @param calendar 등록 내용
   * @return 등록 건수
   */
  @Override
  public Long save(Long memno, String date, Calendar calendar) {
    return calendarDAO.save(memno, date, calendar);
  }


  @Override
  public Long save(Long memno, String date, Calendar calendar, List<MultipartFile> files) {
    Long cdno = calendarDAO.save(memno, date, calendar);
    uploadFileSVC.fileUpload(AttachCode.CD_CODE, cdno, files);

    return cdno;
  }

  /**
   * 운동기록 조회(1회)
   *
   * @param date 조회 날짜
   * @return 조회 내용
   */
  @Override
  public Optional<Calendar> findByDate(String date, Long memno) {
    return calendarDAO.findByDate(date, memno);
  }

  /**
   * 운동기록 수정
   *
   * @param date     수정 날짜
   * @param calendar 수정 내용
   */
  @Override
  public void update(String date, Calendar calendar, Long memno) {
    calendarDAO.update(date, calendar, memno);
  }

  @Override
  public void update(String date, Calendar calendar, List<MultipartFile> files) {
    // 운동기록 수정
    calendarDAO.update(date, calendar, calendar.getMemno());

    // 첨부파일 추가
    uploadFileSVC.fileUpload(AttachCode.CD_CODE, calendar.getCdno(), files);
  }

  @Override
  public void update(String date, Calendar calendar, Long[] deletedFiles) {
    // 운동기록 수정
    calendarDAO.update(date, calendar, calendar.getMemno());

    // 첨부파일 삭제
    uploadFileSVC.delete(deletedFiles);
  }

  @Override
  public void update(String date, Calendar calendar, List<MultipartFile> files, Long[] deletedFiles) {
    // 운동기록 수정
    calendarDAO.update(date, calendar, calendar.getMemno());

    // 첨부파일 추가
    uploadFileSVC.fileUpload(AttachCode.CD_CODE, calendar.getCdno(), files);

    // 첨부파일 삭제
    uploadFileSVC.delete(deletedFiles);
  }

  /**
   * 운동기록 삭제
   *
   * @param date 삭제 날짜
   */
  @Override
  public void del(String date, Long memno) {

    Optional<Calendar> foundRecord = calendarDAO.findByDate(date, memno);
    Long cdno = foundRecord.get().getCdno();

    // 첨부파일 있는지 확인
    List<FileData> foundImagesList = uploadFileSVC.findImages(AttachCode.CD_CODE, cdno);

    // 운동기록 삭제
    calendarDAO.del(date, memno);


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

  /**
   * 달력 조회(1달)
   *
   * @param year 조회 달 첫째날
   * @param month 조회 달 마지막 날
   * @return
   */
  @Override
  public List<Calendar> monthly(String year, String month, Long memno) {
    return calendarDAO.monthly(year, month, memno);
  }

}
