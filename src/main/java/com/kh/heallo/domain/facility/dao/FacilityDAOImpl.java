package com.kh.heallo.domain.facility.dao;

import com.kh.heallo.domain.facility.Bookmark;
import com.kh.heallo.domain.facility.Criteria;
import com.kh.heallo.domain.facility.Facility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FacilityDAOImpl implements FacilityDAO{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean isConnected(Facility facility) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT fcno cnt from facility ");
        sql.append("          where fcname = ? ");
        sql.append("            and fcaddr = ? ");

        Long fcno = null;
        try {
            fcno = jdbcTemplate.queryForObject(sql.toString(), Long.class, facility.getFcname(), facility.getFcaddr());
        } catch (DataAccessException e) {
            log.info("DataAccessException {}", e.getMessage());
        }
        return fcno != null ? true : false;
    }

    @Override
    public Long add(Facility facility) {
        String sql = "insert into facility values (facility_fcno_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"fcno"});
            ps.setString(1, facility.getFcname());
            ps.setString(2, facility.getFctype());
            ps.setString(3, facility.getFchomepage());
            ps.setString(4, facility.getFctel());
            ps.setDouble(5, facility.getFclat());
            ps.setDouble(6, facility.getFclng());
            ps.setString(7, facility.getFcaddr());
            ps.setString(8, facility.getFcpostcode());
            ps.setString(9, facility.getFcstatus());
            ps.setString(10, facility.getFcimg());
            ps.setDouble(11, facility.getFcscore());
            return ps;
        },keyHolder);

        return Long.valueOf(keyHolder.getKeys().get("fcno").toString());
    }

    @Override
    public Integer update(Facility facility) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update facility ");
        sql.append("            set   fcname = ?, fctype = ?, fchomepage = ?, fctel = ?,  ");
        sql.append("                  fclat = ?, fclng = ?, fcaddr = ?, fcpostcode = ?, fcstatus = ? ");
        sql.append("            where fcname = ? or fcaddr = ? ");

        Integer resultCount = jdbcTemplate.update(
                sql.toString(),
                facility.getFcname(),
                facility.getFctype(),
                facility.getFchomepage(),
                facility.getFctel(),
                facility.getFclat(),
                facility.getFclng(),
                facility.getFcaddr(),
                facility.getFcpostcode(),
                facility.getFcstatus(),
                facility.getFcname(),
                facility.getFcaddr()
        );

        return resultCount;
    }

    /**
     * 운동시설 삭제
     * @param fcno
     * @return 결과 수
     */
    @Override
    public Integer delete(Long fcno) {
        String sql = "delete facility where fcno = ? ";

        return jdbcTemplate.update(sql, fcno);
    }

    /**
     * 운동시설 조건검색
     * @param criteria 검색조건
     * @return 운동시설리스트
     */
    @Override
    public List<Facility> search(Criteria criteria) {
        StringBuffer sql = new StringBuffer();
        sql.append("    select * ");
        sql.append("        from (select rownum rowno, facility.* from facility ");
        sql.append("                where fcname like ? ");
        sql.append("                    and fcaddr like ? ");
        sql.append("                    and fctype like ?) fc ");
        sql.append("        where fc.rowno > ? and fc.rowno <= ? ");

        Integer endPage = criteria.getPageNo() * criteria.getNumOfRow();
        Integer startPage = endPage - criteria.getNumOfRow();
        List<Facility> foundFacilityList = null;
        try {
            foundFacilityList = jdbcTemplate.query(
                    sql.toString(),
                    new BeanPropertyRowMapper<>(Facility.class),
                    criteria.getFcname(),
                    criteria.getFcaddr(),
                    criteria.getFctype(),
                    startPage,
                    endPage
            );
        } catch (DataAccessException e) {
            log.info("DataAccessException {}", e.getMessage());
        }

        return foundFacilityList;
    }

    /**
     * 운동시설 조건검색 결과 수
     * @param criteria 검색조건
     * @return 결과 수
     */
    @Override
    public Integer getTotalCount(Criteria criteria) {
        StringBuffer sql = new StringBuffer();
        sql.append("   select count(*) fctotal from facility ");
        sql.append("      where fcname like ? ");
        sql.append("        and fcaddr like ? ");
        sql.append("        and fctype like ? ");

        Integer totalCount = jdbcTemplate.queryForObject(
                sql.toString(),
                Integer.class,
                criteria.getFcname(),
                criteria.getFcaddr(),
                criteria.getFctype()
        );

        return totalCount;
    }

    /**
     * 운동시설 평균평점 수정
     * @param fcno 운동시설번호
     * @return 결과 수
     */
    @Override
    public Integer updateScore(Long fcno) {
        StringBuffer sql = new StringBuffer();
        sql.append("      update facility ");
        sql.append("        set fcscore = (select nvl(round(avg(rvscore),1),0) rvavg from review ");
        sql.append("                        where review.fcno = ?) ");
        sql.append("        where fcno = ? ");

        Integer resultCount = jdbcTemplate.update(sql.toString(), fcno, fcno);

        return resultCount;
    }

    /**
     * 운동시설 상세검색
     * @param fcno 운동시설번호
     * @return 운동시설
     */
    @Override
    public Facility findByFcno(Long fcno) {
        String sql = "select * from facility where fcno = ? ";

        Facility foundFacility = null;
        try {
            foundFacility = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Facility.class), fcno);
        } catch (DataAccessException e) {
            log.info("DataAccessException {}", e.getMessage());
        }

        return foundFacility;
    }

    /**
     * 로그인 계정 즐겨찾기 목록 조회
     * @param memno 회원번호
     * @return 즐겨찾기 리스트
     */
    @Override
    public List<Bookmark> findBookmarkListByMemno(Long memno) {
        String sql = "select bmno,memno,fcno from bookmark where memno = ? ";

        List<Bookmark> bookmarkList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Bookmark.class), memno);

        return bookmarkList;
    }

    /**
     * 즐겨찾기추가
     * @param fcno  운동시설번호
     * @param memno 회원번호
     * @return 즐겨찾기 번호
     */
    @Override
    public Long addBookmark(Long memno,Long fcno) {
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into bookmark ");
        sql.append("    values(bookmark_bmno_seq.nextval, ?, ?) ");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql.toString(), new String[]{"bmno"});
            preparedStatement.setLong(1,memno);
            preparedStatement.setLong(2,fcno);
            return preparedStatement;
        },keyHolder);

        return Long.valueOf(keyHolder.getKeys().get("bmno").toString());
    }

    /**
     * 즐겨찾기 삭제
     * @param bmno 즐겨찾기번호
     * @return 결과 수
     */
    @Override
    public Integer deleteBookmark(Long bmno) {
        String sql = "delete bookmark where  bmno = ? ";

        Integer resultCount = jdbcTemplate.update(sql, bmno);

        return resultCount;
    }
}
