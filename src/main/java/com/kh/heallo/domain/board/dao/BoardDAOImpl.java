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
import org.thymeleaf.util.StringUtils;

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
    sql.append("insert into board(bdno,bdcg,bdtitle,bdcontent,memno  ) ");
    sql.append("     values(board_bdno_seq.nextval, ?, ?, ?, ?  ) ");

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
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT * ");
    sql.append("   FROM (SELECT  ROWNUM AS no, A.* ");
    sql.append("            FROM (SELECT t1.*, t2.memnickname ");
    sql.append("                    FROM board t1, member t2  ");
    sql.append("                   where t1.memno = t2.memno ");
    sql.append("                ORDER BY t1.bdno desc) A) ");

    List<Board> boards = jt.query(sql.toString(), new BeanPropertyRowMapper<>(Board.class));
    return boards;
  }


  @Override
  public List<Board> findAll(int startRec, int endRec) {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT * ");
    sql.append("   FROM (SELECT  ROWNUM AS no, A.* ");
    sql.append("            FROM (SELECT t1.*, t2.memnickname ");
    sql.append("                    FROM board t1, member t2  ");
    sql.append("                   where t1.memno = t2.memno ");
    sql.append("                ORDER BY t1.bdno desc) A) ");
    sql.append("  WHERE no between ? and ? ");

    List<Board> boards = jt.query(sql.toString(), new BeanPropertyRowMapper<>(Board.class), startRec, endRec);
    return boards;
  }


  @Override
  public List<Board> findAll(String bdcg, int startRec, int endRec) {
    StringBuffer sql = new StringBuffer();
//    sql.append("  select no, t3.bdno, t3.bdcg, t3.bdtitle, t3.memno, t3.bdcontent, t3.bdcdate, t3.bdview, t3.memnickname ");
//    sql.append(" from ( ");
//    sql.append("     select rownum no, t1.bdno, t1.bdcg, t1.bdtitle, t1.memno, t1.bdcontent, t1.bdcdate, t1.bdview, t2.memnickname ");
//    sql.append("       from board t1, member t2 ");
//    sql.append("      where t1.memno=t2.memno ");
//    sql.append("      order by t1.bdno desc)t3 ");
//    sql.append(" where no between ? and ? ");
//    sql.append("      and t3.bdcg = ? ");
//    sql.append(" order by no asc ");

    sql.append(" SELECT * ");
    sql.append("   FROM (SELECT  ROWNUM AS no, A.* ");
    sql.append("            FROM (SELECT t1.*, t2.memnickname ");
    sql.append("                    FROM board t1, member t2  ");
    sql.append("                   where t1.memno = t2.memno ");
    sql.append("                     and t1.bdcg = ? ");
    sql.append("                ORDER BY t1.bdno desc) A ) ");
    sql.append("  WHERE no between ? and ? ");

    List<Board> boards = jt.query(sql.toString(), new BeanPropertyRowMapper<>(Board.class), bdcg, startRec, endRec );
    return boards;
  }


//검색어 검색
  @Override
  public List<Board> findAll(BbsFilterCondition filterCondition) {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT * ");
    sql.append("   FROM (SELECT  ROWNUM AS no, A.* ");
    sql.append("            FROM (SELECT t1.*, t2.memnickname ");
    sql.append("                    FROM board t1, member t2  ");
    sql.append("                   where t1.memno = t2.memno ");
    sql.append("                     and t1.bdcg = ? ");

    if(!StringUtils.isEmpty(filterCondition.getCategory()) ||
        !StringUtils.isEmpty(filterCondition.getSearchType()) ||
        !StringUtils.isEmpty(filterCondition.getKeyword())){
      sql.append(" AND ");
    }
    switch (filterCondition.getSearchType()){
      case "A": //전체
        sql.append("   (t1.bdtitle like '%"+filterCondition.getKeyword()+ "%' or ");
        sql.append("    t1.bdcontent like '%"+filterCondition.getKeyword() + "%' or ");
        sql.append("    t2.memnickname like '%"+filterCondition.getKeyword()+ "%' ) ");
        break;
      case "N": //닉네임
        sql.append("    t2.memnickname like '%"+filterCondition.getKeyword() +"%'  ");
        break;
      case "TC": //제목, 내용
        sql.append("   (t1.bdtitle like '%"+filterCondition.getKeyword() +"%' or ");
        sql.append("    t1.bdcontent like '%"+ filterCondition.getKeyword() + "%' ) ");
        break;
      default:
    }

    sql.append(" ORDER BY t1.bdno desc ) A ) ");
    sql.append(" WHERE no between ? and ? ");

    List<Board> boards = jt.query(sql.toString(),
        new BeanPropertyRowMapper<>(Board.class),
        filterCondition.getCategory(),
        filterCondition.getStartRec(),
        filterCondition.getEndRec());

    return boards;
  }



  //조회
  @Override
  public Optional<Board> findByBoardId(Long boardId) {
    StringBuffer sql = new StringBuffer();
    sql.append("select B.bdno, B.bdcg, B.bdtitle, B.bdcdate, B.bdudate, B.bdcontent,B.memno, B.bdview, M.memnickname ");
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
  public int deleteByBoardId(Long BoardId) {
    String sql = "delete from board where bdno = ? ";
    int affectedRow = jt.update(sql.toString(), BoardId);
    return affectedRow;
  }

  @Override
  public int totalCount() {
    String sql = "select count(*) from board";

    int affectedRow = jt.queryForObject(sql.toString(),Integer.class);

    return affectedRow;
  }


  @Override
  public int totalCount(String bdcg) {
    String sql = "select count(*) from board where bdcg = ? ";

    int affectedRow = jt.queryForObject(sql.toString(),Integer.class, bdcg);

    return affectedRow;
  }



  @Override
  public int totalCount(BbsFilterCondition filterCondition) {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT count(*) ");
    sql.append("   FROM (SELECT  ROWNUM AS no, A.* ");
    sql.append("            FROM (SELECT t1.*, t2.memnickname ");
    sql.append("                    FROM board t1, member t2  ");
    sql.append("                   where t1.memno = t2.memno ");
    sql.append("                     and t1.bdcg = ? ");

    if(!StringUtils.isEmpty(filterCondition.getCategory()) ||
        !StringUtils.isEmpty(filterCondition.getSearchType()) ||
        !StringUtils.isEmpty(filterCondition.getKeyword())){
      sql.append(" AND ");
    }

    switch (filterCondition.getSearchType()){
      case "A": //전체
        sql.append("   (t1.bdtitle like '%"+filterCondition.getKeyword()+ "%' or ");
        sql.append("    t1.bdcontent like '%"+filterCondition.getKeyword() + "%' or ");
        sql.append("    t2.memnickname like '%"+filterCondition.getKeyword()+ "%' ) ");
        break;
      case "N": //닉네임
        sql.append("    t2.memnickname like '%"+filterCondition.getKeyword() +"%'  ");
        break;
      case "TC": //제목, 내용
        sql.append("   (t1.bdtitle like '%"+filterCondition.getKeyword() +"%' or ");
        sql.append("    t1.bdcontent like '%"+ filterCondition.getKeyword() + "%' ) ");
        break;
      default:
    }
    sql.append(" ORDER BY t1.bdno desc ) A ) ");

    int affectedRow = jt.queryForObject(sql.toString(),Integer.class,
        filterCondition.getCategory());
    return affectedRow;
  }


//조회수
  @Override
  public int increaseViewCount(Long boardId) {
    StringBuffer sql = new StringBuffer();
    sql.append("update board  ");
    sql.append("set bdview = bdview + 1 ");
    sql.append("where bdno = ? ");

    int affectedRows = jt.update(sql.toString(), boardId);
    return affectedRows;
  }

  //좋아요
//  @Override
//  public int increaseHitCount(Long boardId) {
//    StringBuffer sql = new StringBuffer();
//    sql.append("update board  ");
//    sql.append("set bdhit = bdhit + 1 ");
//    sql.append("where bdno = ? ");
//
//    int affectedRows = jt.update(sql.toString(), boardId);
//    return affectedRows;
//  }
}
