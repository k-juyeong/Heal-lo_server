package com.kh.heallo.domain.reply.dao;


import com.kh.heallo.domain.reply.Reply;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReplyDAOImpl implements ReplyDAO {

  private final JdbcTemplate jdbcTemplate;

  /**
   * 댓글 수
   * @param bdno 게시글 번호
   * @return
   */
  @Override
  public int count(Long bdno) {
    String sql = "SELECT COUNT(RPNO) FROM REPLY WHERE BDNO = ? ";

    int affectedRow = jdbcTemplate.queryForObject(sql, Integer.class, bdno);
    return affectedRow;
  }

  /**
   * 게시글에 해당하는 댓글 목록 조회
   * @return 댓글 목록
   */
  @Override
  public List<Reply> all(Long bdno) {
    StringBuffer sql = new StringBuffer();

    sql.append("select t1.memnickname, t2.* ");
    sql.append("    from member t1, (select t1.rpno, bdno, memno, t1.rpgroup, rpdepth, rpstep, rpstatus, rpcomment, rpudate ");
    sql.append("                       from reply t1 left outer join (select rpno, rpgroup ");
    sql.append("                                                        from reply ");
    sql.append("                                                    group by rpgroup, rpno ");
    sql.append("                                                    order by rpgroup, rpno asc) t2 on t1.rpno = t2.rpno) t2 ");
    sql.append(" where t1.memno = t2.memno ");
    sql.append("   and t2.bdno = ? ");

    List<Reply> list = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Reply.class), bdno);
    return list;
  }

  /**
   * 댓글 등록
   * @param reply 등록할 댓글
   * @return 댓글 번호
   */
  @Override
  public Long save(Long bdno, Long memno, Reply reply) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO REPLY (rpno, bdno, rpgroup, rpdepth, rpstep, rpstatus, memno, rpcomment ) ");
    sql.append("     VALUES (REPLY_RPNO_SEQ.nextval, ?, REPLY_RPNO_SEQ.currval, 0, 0, 'POST', ?, ? ) ");

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql.toString(), new String[]{"rpno"});
        pstmt.setLong(1, bdno);
//        pstmt.setLong(2, reply.getRpGroup());
//        pstmt.setLong(3, reply.getRpDepth());
//        pstmt.setLong(4, reply.getRpStep());
        pstmt.setLong(2, memno);
        pstmt.setString(3, reply.getRpComment());
        return pstmt;
      }
    }, keyHolder);

    return Long.valueOf(keyHolder.getKeys().get("rpno").toString());
  }

  /**
   * 대댓글 등록
   * @param reply 등록할 대댓글
   * @return 댓글 번호
   */
  @Override
  public Long savePlusReply(Long bdno, Long memno, Reply reply) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO REPLY (rpno, bdno, rpgroup, rpdepth, rpstep, rpstatus, memno, rpcomment ) ");
    sql.append("     VALUES (REPLY_RPNO_SEQ.nextval, ?, ?, ?, 1, 'POST', ?, ? ) ");

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql.toString(), new String[]{"rpno"});
        pstmt.setLong(1, bdno);
        pstmt.setLong(2, reply.getRpGroup());
        pstmt.setLong(3, reply.getRpDepth());
//        pstmt.setLong(4, reply.getRpStep());
        pstmt.setLong(4, memno);
        pstmt.setString(5, reply.getRpComment());
        return pstmt;
      }
    }, keyHolder);

    return Long.valueOf(keyHolder.getKeys().get("rpno").toString());

  }

  /**
   * 댓글 수정
   *
   * @param rpno  수정할 댓글 번호
   * @param reply 수정할 댓글 내용
   */
  @Override
  public void update(Long rpno, Reply reply) {
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE REPLY ");
    sql.append("   SET RPCOMMENT = ?, ");
    sql.append("       RPUDATE = SYSDATE ");
    sql.append(" WHERE RPNO = ? ");

    jdbcTemplate.update(sql.toString(), reply.getRpComment(), rpno);
  }

  /**
   * 댓글 삭제
   *
   * @param rpno 삭제할 댓글 번호
   */
  @Override
  public void delete(Long rpno) {
    String sql = "DELETE FROM REPLY WHERE RPNO = ? ";

    jdbcTemplate.update(sql, rpno);
  }

  /**
   * 대댓글이 있는 경우 상태만 변경
   *
   * @param rpno 삭제할 댓글 번호
   */
  @Override
  public void deleteState(Long rpno) {
    StringBuffer sql = new StringBuffer();

    sql.append("UPDATE REPLY ");
    sql.append("   SET RPSTATUS = 'DELETED' ");
    sql.append(" WHERE RPNO = ? ");

    jdbcTemplate.update(sql.toString(), rpno);
  }

  /**
   * 'DELETED' 상태의 댓글의 대댓글이 모두 삭제된 경우
   * 해당 댓글도 삭제
   *
   * @param rpno 삭제할 댓글 번호
   */
  @Override
  public void deleteFinally(Long rpno) {

  }

  /**
   * 게시글 삭제할 경우 해당 게시글의 모든 댓글 삭제
   * @param bdno
   */
  @Override
  public void deleteAll(Long bdno) {
    String sql = "DELETE FROM REPLY WHERE bdno = ? ";
    jdbcTemplate.update(sql, bdno);
  }

  /**
   * 대댓글의 경우 step을 한단계 더함
   * @param rpno 해당 대댓글 번호
   */
  @Override
  public void reReplyStep(Long rpno) {
    StringBuffer sql = new StringBuffer();

    sql.append("UPDATE REPLY ");
    sql.append("SET RPSTEP = RPSTEP + 1 ");
    sql.append("WHERE RPNO = ? ");

    jdbcTemplate.update(sql.toString(), rpno);
  }

  /**
   * 대댓글의 대댓글 경우 그 이후 rpStep 번호 수정 (+1)
   * @param rpGroup 부모댓글
   * @param rpDepth 들여쓰기 정도
   * @param rpno 대댓글 번호 (rpDepth 사용)
   */
  @Override
  public void updatedStep(Long rpGroup,Long rpDepth, Long rpno) {
    StringBuffer sql = new StringBuffer();

    sql.append("UPDATE REPLY ");
    sql.append("   SET RPSTEP = RPSTEP + 1 ");
    sql.append(" WHERE RPGROUP = ? ");
    sql.append("   AND RPDEPTH BETWEEN 1 AND ? ");
    sql.append("   AND RPNO >= ? ");

    jdbcTemplate.update(sql.toString(), rpDepth, rpDepth, rpno);
  }
}
