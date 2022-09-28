package com.kh.heallo.domain.bookmark.dao;

import com.kh.heallo.domain.bookmark.Bookmark;
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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookmarkDAOImpl implements BookmarkDAO{

    private final JdbcTemplate jdbcTemplate;

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
     * 즐겨찾기 추가여부 확인
     *
     * @param memno
     * @param fcno
     * @return
     */
    @Override
    public Optional<Bookmark> CheckingBookmark(Long memno, Long fcno) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from bookmark ");
        sql.append(" where memno = ? and fcno = ? ");

        Optional<Bookmark> optionalBookmark = null;
        try {
            Bookmark bookmark = jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(Bookmark.class), memno, fcno);
            optionalBookmark = Optional.of(bookmark);
        } catch (DataAccessException e) {
            optionalBookmark = Optional.empty();
        }

        return optionalBookmark;
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

    /**
     * 즐겨찾기 페이지 리스트
     *
     * @return
     */
    @Override
    public List<Facility> bookmarkPageList(String order) {
        StringBuffer sql = new StringBuffer();

        sql.append(" select FACILITY.FCNO fcno, FACILITY.FCIMG fcimg,FACILITY.FCADDR fcaddr, FACILITY.FCTEL fctel, FACILITY.FCSCORE fcscore, FACILITY.FCNAME fcname ");
        sql.append("   from BOOKMARK, FACILITY, MEMBER ");
        sql.append("  where BOOKMARK.FCNO=FACILITY.FCNO and BOOKMARK.memno = MEMBER.memno ");
        sql.append("  order by " + order + " ");

        List<Facility> bookmarks = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Facility.class));

        return bookmarks;
    }
}
