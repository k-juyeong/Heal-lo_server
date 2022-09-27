package com.kh.heallo.domain.calendar.dao;

import com.kh.heallo.domain.calendar.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class CalendarDAOImplTest {

  @Autowired
  private CalendarDAO calendarDAO;

  @Test
  @DisplayName("운동기록 저장")
  void save() {
    Calendar calendar = new Calendar();
    calendar.setMemno(Long.valueOf("3"));
    calendar.setCdContent("운동함");
    calendar.setCdRDate("2022-09-28");
    calendar.setCdCDate(LocalDateTime.now());
    calendar.setCdUDate(LocalDateTime.now());
    String rdate = "2022-09-28";

    Long savedRecord = calendarDAO.save(rdate, calendar);
//    log.info("saveRecord={}", savedRecord);
  }

  @Test
  @DisplayName("운동기록 조회")
  void findByDate() {
    String date = "2022-09-28";
    Optional<Calendar> foundRecord = calendarDAO.findByDate(date);
    if (foundRecord.isPresent()) {
      Assertions.assertThat(foundRecord.get().getCdno()).isEqualTo(3);
      Assertions.assertThat(foundRecord.get().getCdContent()).isEqualTo("운동함");
      Assertions.assertThat(foundRecord.get().getCdRDate()).isEqualTo("2022-09-28");

    }

  }

  @Test
  @DisplayName("운동기록 수정")
  void update() {
    String date = "2022-09-28";


  }

  @Test
  @DisplayName("운동기록 삭제")
  void del() {

  }

  @Test
  @DisplayName("운동기록 조회(1달)")
  void monthly() {

  }
}