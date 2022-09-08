package com.kh.heallo.domain.bookmark.dao;

import com.kh.heallo.domain.bookmark.Bookmark;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
