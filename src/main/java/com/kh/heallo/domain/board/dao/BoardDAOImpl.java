package com.kh.heallo.domain.board.dao;

import com.kh.heallo.domain.board.Board;
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
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardDAOImpl implements BoardDAO{
  private final JdbcTemplate jt;

  /**
   * 등록
   * @param board
   * @return
   */
  @Override
  public Long save(Board board) {
    StringBuffer sql = new StringBuffer();
    sql.append("insert into board(bdno,bdcg,bdtitle,bdcontent,memno ) ");
    sql.append("     values(board_bdno_seq.nextval, ?, ?, ?, ? ) ");

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jt.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql.toString(), new String[]{"bdno"});
        pstmt.setString(1, board.getBdcg());
        pstmt.setString(2, board.getBdtitle());
        pstmt.setString(3, board.getBdcontent());
        pstmt.setLong(4, board.getMemno());
        return pstmt;
      }
    },keyHolder);
    return Long.valueOf(keyHolder.getKeys().get("bdno").toString());
  }

  //목록
  @Override
  public List<Board> findAll() {
//    StringBuffer sql = new StringBuffer();
//    sql.append("select bdno,bdcg,bdtitle,bdcdate,memno ");
//    sql.append("  from board ");
//    sql.append("order by bdno desc ");

    StringBuffer sql = new StringBuffer();
    sql.append("select B.bdno, B.bdcg, B.bdtitle, B.bdcdate, B.bdudate, B.memno, M.memnickname ");
    sql.append("  from board B, member M ");
    sql.append("  where M.memno=B.memno ");
    sql.append("order by bdno desc ");


    List<Board> boards = jt.query(sql.toString(), new BeanPropertyRowMapper<>(Board.class));

    return boards;
  }

  //조회
  @Override
  public Optional<Board> findByBoardId(Long boardId) {
//    StringBuffer sql = new StringBuffer();
//    sql.append("select bdno, bdcg, bdtitle, bdcontent, bdcdate, bdudate,memno ");
//    sql.append("  from board ");
//    sql.append(" where bdno = ? ");

    StringBuffer sql = new StringBuffer();
    sql.append("select B.bdno, B.bdcg, B.bdtitle, B.bdcdate, B.bdudate, B.bdcontent,B.memno, M.memnickname ");
    sql.append("  from board B, member M ");
    sql.append(" where M.memno=B.memno and B.bdno= ? ");

    try {
      Board board = jt.queryForObject(
          sql.toString(),
          new BeanPropertyRowMapper<>(Board.class), boardId);
      return Optional.of(board);
    }catch (EmptyResultDataAccessException e){
      e.printStackTrace();
      return Optional.empty();
    }
  }


  //수정
  @Override
  public int update(Long BoardId, Board board) {
    StringBuffer sql = new StringBuffer();
    sql.append("update board ");
    sql.append("   set bdcg = ?, ");
    sql.append("       memno = ?, ");
    sql.append("       bdtitle = ?, ");
    sql.append("       bdcontent = ?, ");
    sql.append("       bdudate = systimestamp ");
    sql.append(" where bdno = ? ");

    int affectedRow = jt.update(sql.toString(),
        board.getBdcg(),board.getMemno(), board.getBdtitle(), board.getBdcontent(),BoardId);
    return affectedRow;
  }


  //삭제
  @Override
  public int deleteByBoardId(Long boardId) {
    String sql = "delete from board where bdno = ? ";

    int affectedRow = jt.update(sql.toString(), boardId);

    return affectedRow;
  }
}
