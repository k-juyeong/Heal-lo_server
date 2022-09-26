package com.kh.heallo.domain.facility.dao;

import com.kh.heallo.domain.facility.AutoComplete;
import com.kh.heallo.domain.facility.FacilityCriteria;
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

    /**
     * 운동시설 중복체크
     * @param facility 운동시설
     * @return true,false
     */
    @Override
    public boolean contains(Facility facility) {
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

    /**
     * 운동시설 등록
     * @param facility 운동시설
     * @return 시퀀스 번호
     */
    //등록
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

    /**
     * 운동시설 업데이트
     * @param facility 운동시설
     * @return 결과 수
     */
    //수정
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
     * @return 결과 수
     */
    //삭제
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
    //조건검색(페이징)
    @Override
    public List<Facility> search(FacilityCriteria criteria) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select fc3.* ");
        sql.append(" from (select rownum rowno, fc2.* ");
        sql.append("         from (select fc.*,(select count(*) from review where fcno = fc.fcno) rvtotal ");
        sql.append("               from facility fc ");
        sql.append("               where REPLACE(TRIM(fcname),' ','') like ? ");
        sql.append("                    and fcaddr like ? ");
        sql.append("                    and fctype like ? ");
        sql.append("               order by rvtotal desc) fc2) fc3 ");
        sql.append(" where fc3.rowno >= ? and fc3.rowno <= ? ");

        log.info("criteria {} ", criteria);

        List<Facility> foundFacilityList = jdbcTemplate.query(
                sql.toString(),
                new BeanPropertyRowMapper<>(Facility.class),
                criteria.getFcname(),
                criteria.getFcaddr(),
                criteria.getFctype(),
                criteria.getStartNo(),
                criteria.getEndNo()
        );

        if (foundFacilityList.size() == 0) {
            throw new DataAccessException("데이터를 찾을수 없습니다") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }

        return foundFacilityList;
    }

    /**
     * 상호명 검색 자동완성
     *
     * @param criteria 검색조건
     * @param row      자동완성 레코드 수
     * @return 상호명 리스트
     */
    @Override
    public List<AutoComplete> autoComplete(FacilityCriteria criteria, Integer row) {
        StringBuffer sql = new StringBuffer();

        sql.append(" select fc2.fcname, rvtotal ");
        sql.append("    from (select distinct REPLACE(TRIM(fc.fcname),' ','') fcname,(select count(*) from review where fcno = fc.fcno) rvtotal ");
        sql.append("            from facility fc ");
        sql.append("                where fcname like ? ");
        sql.append("                    and fcaddr like ? ");
        sql.append("                    and fctype like ? ");
        sql.append("                order by rvtotal desc) fc2 ");
        sql.append("    where rownum <= ? ");

        List<AutoComplete> autoCompleteData = jdbcTemplate.query(
                sql.toString(),
                new BeanPropertyRowMapper<>(AutoComplete.class),
                criteria.getFcname(),
                criteria.getFcaddr(),
                criteria.getFctype(),
                row);

        return autoCompleteData;
    }

    /**
     * 운동시설 조건검색 totalCount
     * @param criteria 검색조건
     * @return 결과 수
     */
    //조검검색 total
    @Override
    public Integer getTotalCount(FacilityCriteria criteria) {
        StringBuffer sql = new StringBuffer();
        sql.append("   select count(*) fctotal from facility ");
        sql.append("      where REPLACE(TRIM(fcname),' ','') like ? ");
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
     * @return  결과 수
     */
    //리뷰평균 업데이트
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
     * @return  운동시설
     */
    //운동시설 상세조회
    @Override
    public Facility findByFcno(Long fcno) {
        String sql = "select * from facility where fcno = ? ";

        Facility foundFacility = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Facility.class), fcno);

        return foundFacility;
    }

}
