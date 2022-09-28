package com.kh.heallo.domain.facility.dao;

import com.kh.heallo.domain.facility.AutoComplete;
import com.kh.heallo.domain.facility.FacilityCriteria;
import com.kh.heallo.domain.facility.Facility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        sql.append(" SELECT count(*) from facility ");
        sql.append("          where  fcaddr = ? ");
        sql.append("          and    fcname = ? ");

        Integer count = 0;
        try {
            count += jdbcTemplate.queryForObject(
                    sql.toString(),
                    Integer.class,
                    facility.getFcaddr(),
                    facility.getFcname()
            );
        } catch (Exception e) {
            log.info("Exception {}", e.getMessage());
            count += 1;
        }
        return count == 1 ? true : false;
    }

    /**
     * 운동시설 등록
     * @param facilityList 운동시설
     * @return 시퀀스 번호
     */
    //등록
    @Override
    public int[] add(List<Facility> facilityList) {
        String sql = "insert into facility values (facility_fcno_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, facilityList.get(i).getFcname());
                ps.setString(2, facilityList.get(i).getFctype());
                ps.setString(3, facilityList.get(i).getFchomepage());
                ps.setString(4, facilityList.get(i).getFctel());
                ps.setDouble(5, facilityList.get(i).getFclat());
                ps.setDouble(6, facilityList.get(i).getFclng());
                ps.setString(7, facilityList.get(i).getFcaddr());
                ps.setString(8, facilityList.get(i).getFcpostcode());
                ps.setString(9, facilityList.get(i).getFcstatus());
                ps.setString(10, facilityList.get(i).getFcimg());
                ps.setDouble(11, facilityList.get(i).getFcscore());
            }

            //배치처리할 건수
            @Override
            public int getBatchSize() {
                return facilityList.size();
            }
        });

        return results;
    }

    /**
     * 운동시설 업데이트
     * @param facilityList 운동시설
     * @return 결과 수
     */
    //수정
    @Override
    public int[] update(List<Facility> facilityList) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update facility ");
        sql.append("            set   fcname = ?, fctype = ?, fchomepage = ?, fctel = ?,  ");
        sql.append("                  fcpostcode = ?, fcstatus = ? ");
        sql.append("            where fcaddr = ? or fclat = ? and fclng = ? ");

        int[] results = jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1,facilityList.get(i).getFcname());
                ps.setString(2,facilityList.get(i).getFctype());
                ps.setString(3,facilityList.get(i).getFchomepage());
                ps.setString(4,facilityList.get(i).getFctel());
                ps.setString(5,facilityList.get(i).getFcpostcode());
                ps.setString(6,facilityList.get(i).getFcstatus());
                ps.setString(7,facilityList.get(i).getFcaddr());
                ps.setDouble(8,facilityList.get(i).getFclat());
                ps.setDouble(9,facilityList.get(i).getFclng());
            }

            //배치처리할 건수
            @Override
            public int getBatchSize() {
                return facilityList.size();
            }
        });

        return results;
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
