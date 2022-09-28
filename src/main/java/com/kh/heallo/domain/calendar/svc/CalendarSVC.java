package com.kh.heallo.domain.calendar.svc;

import com.kh.heallo.domain.calendar.Calendar;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CalendarSVC {


  /**
   * 운동기록 등록
   * @param date 운동기록 날짜
   * @param calendar 등록 내용
   * @return 등록 번호
   */
  Long save(Long memno, String date, Calendar calendar);

  Long save(Long memno, String date, Calendar calendar, List<MultipartFile> files);

  /**
   * 운동기록 조회(1회)
   * @param date 조회 날짜
   * @return 조회 내용
   */
  Optional<Calendar> findByDate(String date, Long memno);

  /**
   * 운동기록 수정
   * @param date 수정 날짜
   * @param calendar 수정 내용
   */
  void update(String date, Calendar calendar, Long memno);
  // 운동기록 수정 중 파일 추가
  void update(String date, Calendar calendar, List<MultipartFile> files);

  // 운동기록 수정 중 파일 삭제
  void update(String date, Calendar calendar, Long[] deletedFiles);
  // 운동기록 수정 중 파일 삭제, 파일 추가
  void update(String date, Calendar calendar, List<MultipartFile> files, Long[] deletedFiles);

  /**
   * 운동기록 삭제
   * @param date 삭제 날짜
   */
  void del(String date, Long memno);



  /**
   * 달력 조회(1달)
   * @param year 조회 달 첫째날
   * @param month 조회 달 마지막 날
   * @return
   */
  List<Calendar> monthly(String year, String month, Long memno);
}
