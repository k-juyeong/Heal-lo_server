package com.kh.heallo.domain.common.code;

import com.kh.heallo.web.Code;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CodeDAOImpl implements CodeDAO{
  private final JdbcTemplate jt;

  @Override
  public List<com.kh.heallo.web.Code> code(String pcode) {
    StringBuffer sql = new StringBuffer();
    sql.append("  select t1.code, t1.code_name decode  ");
    sql.append("    from code t1, code t2 ");
    sql.append("   where t1.pcode = t2.code ");
    sql.append("     and t1.use_status = 'Y'  ");
    sql.append("     and t1.pcode = ? ");

    List<Code> codes = jt.query(
        sql.toString(),
        new BeanPropertyRowMapper<>(Code.class),
        pcode
    );
    return codes;
  }


  //모든 코드 반환
  @Override
  public List<CodeAll> codeAll() {
    StringBuffer sql = new StringBuffer();
    sql.append("select t1.pcode, t2.code_name pdecode, t1.code ccode, t1.code_name cdecode  ");
    sql.append("  from code t1, code t2 ");
    sql.append(" where t1.pcode = t2.code  ");
    sql.append("   and t1.use_status = 'Y' ");

    List<CodeAll> codeAll = jt.query(
        sql.toString(),
        new BeanPropertyRowMapper<>(CodeAll.class)
    );
    return codeAll;
  }
}
