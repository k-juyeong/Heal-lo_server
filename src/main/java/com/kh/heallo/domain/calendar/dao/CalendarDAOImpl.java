package com.kh.heallo.domain.calendar.dao;

import com.kh.heallo.domain.calendar.Calendar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CalendarDAOImpl implements CalendarDAO{
  private final JdbcTemplate jt;

  // 등록
  @Override
  public int save(String date, Calendar calendar) {
    int result = 0;
    StringBuffer sql = new StringBuffer();
    sql.append("insert into calendar (cdno, memno, cdContent, cdrdate, cdcdate, cdudate) ");
    sql.append("values (calendar_cdno_seq.nextval, 2, ?, ?, sysdate, sysdate) ");

    result = jt.update(sql.toString(), calendar.getCdContent(), calendar.getCdRDate());
    return result;
  }

  // 달력 번호 생성
  @Override
  public Integer createCdno() {
    String sql = "select calendar_cdno_seq.nextval from dual ";
    int cdno = jt.update(sql, Integer.class);
    return cdno;
  }

  // 조회 (날짜 클릭 => 1일 조회)
  @Override
  public Calendar findByDate(String date) {
    StringBuffer sql = new StringBuffer();

    // 로그인 후 회원 번호 쿼리 추가
    sql.append("select cdcontent, cdrdate, cdcdate ");
    sql.append("  from calendar ");
    sql.append(" where cdrdate = ? ");

    Calendar selectedCalendar = null;
    try {
      selectedCalendar = jt.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(Calendar.class), date);
    } catch (DataAccessException e) {
      log.info("해당 날짜의 정보를 찾을 수 없음={}", date);
    }
    return selectedCalendar;
  }

  // 수정
  @Override
  public void update(String date, Calendar calendar) {
    StringBuffer sql = new StringBuffer();
    sql.append("update calendar ");
    sql.append("   set cdcontent = ? ");
    sql.append("       cdudate = sysdate ");
    sql.append(" where cdrdate = ? ");

    jt.update(sql.toString(), calendar.getCdContent(), date);
  }

  // 삭제
  @Override
  public void del(String date) {
    String sql = "delete from CALENDAR where cdrdate = ? ";

    jt.update(sql, date);

  }


  // 달력 조회 (1달)
  @Override
  public List<Calendar> monthly(String startDate, String finalDate) {
    StringBuffer sql = new StringBuffer();
    sql.append("select cdcontent, cdrdate, cdcdate ");
    sql.append("  from calendar ");
    sql.append( "where cdrdate between ? and ?; ");


    return jt.query(sql.toString(), new BeanPropertyRowMapper<>(Calendar.class), startDate, finalDate);
  }
}
