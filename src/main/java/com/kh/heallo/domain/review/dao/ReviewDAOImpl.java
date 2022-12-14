package com.kh.heallo.domain.review.dao;

import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.ReviewCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ReviewDAOImpl implements ReviewDAO{

    private final JdbcTemplate jdbcTemplate;

    /**
     * 리뷰 조회 결과 수
     * @param fcno 회원번호
     * @return 결과 수
     */
    @Override
    public Integer getTotalCount(Long fcno) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(fcno) rvtotal from review ");
        sql.append(" where fcno = ? ");

        return jdbcTemplate.queryForObject(sql.toString(), Integer.class, fcno);
    }

    /**
     * 리뷰 단일조회
     * @param rvno 리뷰번호
     * @return 리뷰
     */
    @Override
    public Review findByRvno(Long rvno) {
        String sql = "select * from review where rvno = ? ";

        Review review = null;
        review = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Review.class), rvno);

        return review;
    }

    /**
     * 리뷰 조회(페이징)
     * @param fcno 회원번호
     * @param criteria 검색조건
     * @return 리뷰 리스트
     */
    @Override
    public List<Review> findListByFcno(Long fcno, ReviewCriteria criteria) {
        StringBuffer sql = new StringBuffer();
        sql.append("  select * ");
        sql.append("          from (select rownum rowno, review.* ,member.memnickname ");
        sql.append("                  from (select * from review ");
        sql.append("                          order by " + criteria.getOrderBy() + ") review, member ");
        sql.append("                  where review.memno = member.memno ");
        sql.append("                  and review.fcno = ?) ");
        sql.append("  where rowno >= ? and rowno <= ? ");

        List<Review> reviewList = jdbcTemplate.query(sql.toString(), new RowMapper<Review>() {
            @Override
            public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                Review review = new Review();
                review.setRvno(rs.getLong(2));
                review.setRvcontents(rs.getString(3));
                review.setRvline(rs.getInt(4));
                review.setRvscore(rs.getDouble(5));
                review.setRvcdate(rs.getTimestamp(6).toLocalDateTime());
                review.setRvudate(rs.getTimestamp(7).toLocalDateTime());
                review.setMemno(rs.getLong(8));
                review.setFcno(rs.getLong(9));

                Member member = new Member();
                member.setMemnickname(rs.getString(10));
                review.setMember(member);

                return review;
            }
        },fcno,criteria.getStartNo(),criteria.getEndNo());

        if (reviewList.isEmpty()) {
            throw new DataAccessException("데이터를 찾을수 없습니다") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }

        return reviewList;
    }

    /**
     * 리뷰등록
     * @param review 리뷰
     * @return 리뷰번호
     */
    @Override
    public Long add(Review review) {
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into review ");
        sql.append(" values(review_rvno_seq.nextval, ?, ?, ?, sysdate, sysdate, ?, ?) ");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql.toString(), new String[]{"rvno"});
            preparedStatement.setString(1, review.getRvcontents());
            preparedStatement.setInt(2, review.getRvline());
            preparedStatement.setDouble(3, review.getRvscore());
            preparedStatement.setLong(4, review.getMemno());
            preparedStatement.setLong(5, review.getFcno());

            return preparedStatement;
        }, keyHolder);


        return Long.valueOf(keyHolder.getKeys().get("rvno").toString());
    }

    /**
     * 리뷰수정
     * @param review 리뷰
     * @return 결과 수
     */
    @Override
    public Integer update(Review review) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update review set ");
        sql.append("         rvcontents = ?, ");
        sql.append("         rvline = ?, ");
        sql.append("         rvscore = ?, ");
        sql.append("         rvudate = sysdate ");
        sql.append(" where review.rvno = ? ");

        return jdbcTemplate.update(
                sql.toString(),
                review.getRvcontents(),
                review.getRvline(),
                review.getRvscore(),
                review.getRvno()
        );
    }

    /**
     * 리뷰삭제
     * @param rvno 리뷰번호
     * @return 결과 수
     */
    @Override
    public Integer delete(Long rvno) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete review ");
        sql.append(" where rvno = ? ");

        return jdbcTemplate.update(sql.toString(), rvno);
    }
}
