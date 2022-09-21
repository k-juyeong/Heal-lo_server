package com.kh.heallo.domain.calendar.svc;

import com.kh.heallo.domain.calendar.Calendar;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CalendarSVC {
  /**
   * 운동기록 등록
   * @param date 운동기록 날짜
   * @param calendar 등록 내용
   * @return 등록 건수
   */
  Long save(String date, Calendar calendar);

  Long save(String date, Calendar calendar, MultipartFile img);
  Long save(String date, Calendar calendar, List<MultipartFile> imgs);

  /**
   * 운동기록 조회(1회)
   * @param date 조회 날짜
   * @return 조회 내용
   */
  Calendar findByDate(String date);

  /**
   * 운동기록 수정
   * @param date 수정 날짜
   * @param calendar 수정 내용
   */
  void update(String date, Calendar calendar);

  /**
   * 운동기록 삭제
   * @param date 삭제 날짜
   */
  void del(String date);



  /**
   * 달력 조회(1달)
   * @param startDate 조회 달 첫째날
   * @param finalDate 조회 달 마지막 날
   * @return
   */
  List<Calendar> monthly(String startDate, String finalDate);
}
