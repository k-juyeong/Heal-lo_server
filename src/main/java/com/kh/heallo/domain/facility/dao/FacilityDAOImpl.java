package com.kh.heallo.domain.facility.dao;

import com.kh.heallo.domain.facility.Bookmark;
import com.kh.heallo.domain.facility.Criteria;
import com.kh.heallo.domain.facility.Facility;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FacilityDAOImpl implements FacilityDAO{

    private final JdbcTemplate jdbcTemplate;

    /**
     * 공공데이터 연동
     * @param facility 운동시설
     * @return 결과 수
     */
    @Override
    public int connect(Facility facility) {
        StringBuffer sql = new StringBuffer();
        sql.append(" MERGE INTO facility fc1 ");
        sql.append(" USING (SELECT count(*) cnt from facility ");
        sql.append("          where fcname = ? ");
        sql.append("            and fcaddr = ?) fc2 ");
        sql.append(" ON (cnt > 0) ");
        sql.append(" WHEN MATCHED ");
        sql.append(" THEN update set   fcname = ?, fctype = ?, fchomepage = ?, fctel = ?, ");
        sql.append("                   fclat = ?, fclng = ?, fcaddr = ?, fcpostcode = ?, fcstatus = ? ");
        sql.append("             where fcname = ? or fcaddr = ? ");
        sql.append(" WHEN NOT MATCHED ");
        sql.append(" THEN insert values (facility_fcno_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");

        int resultCount = jdbcTemplate.update(
                sql.toString(),
                facility.getFcname(), facility.getFcaddr(),
                facility.getFcname(), facility.getFctype(), facility.getFchomepage(), facility.getFctel(),
                facility.getFclat(), facility.getFclng(), facility.getFcaddr(), facility.getFcpostcode(), facility.getFcstatus(),
                facility.getFcname(), facility.getFcaddr(),
                facility.getFcname(), facility.getFctype(), facility.getFchomepage(), facility.getFctel(),
                facility.getFclat(), facility.getFclng(), facility.getFcaddr(), facility.getFcpostcode(), facility.getFcstatus(),
                facility.getFcimg(), facility.getFcscore()
        );

        return resultCount;
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

        int endPage = criteria.getPageNo() * criteria.getNumOfRow();
        int startPage = endPage - criteria.getNumOfRow();
        List<Facility> foundFacilityList = jdbcTemplate.query(
                sql.toString(),
                new BeanPropertyRowMapper<>(Facility.class),
                criteria.getFcname(),
                criteria.getFcaddr(),
                criteria.getFctype(),
                startPage,
                endPage
        );

        return foundFacilityList;
    }

    /**
     * 운동시설 조건검색 결과 수
     * @param criteria 검색조건
     * @return 결과 수
     */
    @Override
    public int getTotalCount(Criteria criteria) {
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
    public int updateScore(Long fcno) {
        StringBuffer sql = new StringBuffer();
        sql.append("      update facility ");
        sql.append("        set fcscore = (select nvl(round(avg(rvscore),1),0) rvavg from review ");
        sql.append("                        where review.fcno = ?) ");
        sql.append("        where fcno = ? ");

        int resultCount = jdbcTemplate.update(sql.toString(), fcno, fcno);

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

        Facility foundFacility = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Facility.class), fcno);

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
    public int deleteBookmark(Long bmno) {
        String sql = "delete bookmark where  bmno = ? ";

        int resultCount = jdbcTemplate.update(sql, bmno);

        return resultCount;
    }
}
