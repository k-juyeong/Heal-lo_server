package com.kh.heallo.domain.calendar.dao;

import com.kh.heallo.domain.calendar.Calendar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CalendarDAOImpl implements CalendarDAO{
  private final JdbcTemplate jt;

  // 등록
  @Override
  public Long save(Long memno, String date, Calendar calendar) {
    StringBuffer sql = new StringBuffer();
    sql.append("insert into calendar (cdno, memno, cdContent, cdrdate, cdcdate, cdudate) ");
    sql.append("values (calendar_cdno_seq.nextval, ?, ?, ?, sysdate, sysdate) ");

    KeyHolder keyHolder = new GeneratedKeyHolder();


    jt.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql.toString(), new String[]{"cdno"});
        pstmt.setLong(1, calendar.getMemno());
        pstmt.setString(2, calendar.getCdContent());
        pstmt.setString(3, calendar.getCdRDate());
        return pstmt;
      }
    }, keyHolder);


    return Long.valueOf(keyHolder.getKeys().get("cdno").toString());
  }

  // 조회 (날짜 클릭 => 1일 조회)
  @Override
  public Optional<Calendar> findByDate(String date) {
    StringBuffer sql = new StringBuffer();

    // 로그인 후 회원 번호 쿼리 추가

    sql.append("select cdcontent, cdrdate, cdcdate, cdudate ");
    sql.append("  from calendar ");
    sql.append(" where to_char(cdrdate, 'YYYY-MM-DD') = ? ");

    try {
      Calendar selectedCalendar = jt.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(Calendar.class), date);
      return Optional.of(selectedCalendar);
    } catch (EmptyResultDataAccessException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  // 수정
  @Override
  public void update(String date, Calendar calendar) {
    StringBuffer sql = new StringBuffer();
    sql.append("update calendar ");
    sql.append("   set cdcontent = ?, ");
    sql.append("       cdudate = sysdate ");
    sql.append(" where to_char(cdrdate, 'YYYY-MM-DD') = ? ");

    jt.update(sql.toString(), calendar.getCdContent(), date);
  }

  // 삭제
  @Override
  public void del(String date) {
    String sql = "delete from CALENDAR where to_char(cdrdate, 'YYYY-MM-DD') = ? ";

    jt.update(sql, date);

  }


  // 달력 조회 (1달)
  @Override
  public List<Calendar> monthly(String year, String month) {
    StringBuffer sql = new StringBuffer();
    sql.append("select cdcontent, cdrdate, cdcdate ");
    sql.append("  from calendar ");
    sql.append( "where to_char(cdrdate, 'YYYY-MM-DD') between ? and ? ");

    // 전체 날짜 구하기
    int selectedYear = Integer.parseInt(year);
    int selectMonth = Integer.parseInt(month);
    LocalDate totalDate = LocalDate.of(selectedYear, selectMonth, 1);

    int lengthOfMonth = totalDate.lengthOfMonth();


    String yearMonth = String.valueOf(totalDate).substring(0, 7);
    String firstDateOfMonth = yearMonth + "-01";
    String lastDateOfMonth = yearMonth + "-"+ lengthOfMonth;

    return jt.query(sql.toString(), new BeanPropertyRowMapper<>(Calendar.class), firstDateOfMonth, lastDateOfMonth);
  }
}
