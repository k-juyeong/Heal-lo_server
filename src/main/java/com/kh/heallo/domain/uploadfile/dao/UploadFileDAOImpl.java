package com.kh.heallo.domain.uploadfile.dao;

import com.kh.heallo.domain.uploadfile.FileData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UploadFileDAOImpl implements UploadFileDAO {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 파일 등록 단건
     * @param fileData 파일
     * @return 결과
     */
    @Override
    public Integer fileUpload(FileData fileData) {
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into uploadfile ");
        sql.append(" values(uploadfile_ufno_seq.nextval,?,?, ");
        sql.append("         ?,?,?,?,?, ");
        sql.append("         systimestamp,systimestamp) ");

        Integer resultCount = jdbcTemplate.update(
                sql.toString(),
                fileData.getCode(),
                fileData.getNoid(),
                fileData.getUfsname(),
                fileData.getUffname(),
                fileData.getUfsize(),
                fileData.getUftype(),
                fileData.getUfpath()
        );

        return resultCount;
    }

    /**
     * 파일 등록 - 여러건
     * @param fileDataList
     */
    @Override
    public void fileUpload(List<FileData> fileDataList) {
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into uploadfile ");
        sql.append(" values(uploadfile_ufno_seq.nextval,?,?, ");
        sql.append("         ?,?,?,?,?, ");
        sql.append("         systimestamp,systimestamp) ");

        //배치 처리 : 여러건의 갱신작업을 한꺼번에 처리하므로 단건처리할때보다 성능이 좋다.
        jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, fileDataList.get(i).getCode());
                ps.setLong(2,fileDataList.get(i).getNoid());
                ps.setString(3, fileDataList.get(i).getUfsname());
                ps.setString(4, fileDataList.get(i).getUffname());
                ps.setLong(5, fileDataList.get(i).getUfsize());
                ps.setString(6, fileDataList.get(i).getUftype());
                ps.setString(7, fileDataList.get(i).getUfpath());
            }

            //배치처리할 건수
            @Override
            public int getBatchSize() {
                return fileDataList.size();
            }
        });
    }

    /**
     * 이미지파일 조회(복수)
     * @param code
     * @param noid
     * @return 이미지 리스트
     */
    @Override
    public List<FileData> findImages(String code, Long noid) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * ");
        sql.append(" from uploadfile ");
        sql.append(" where code = ? ");
        sql.append("  and noid = ? ");

        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(FileData.class), code, noid);
    }

    /**
     * 파일조회
     * @param ufno
     * @return
     */
    @Override
    public FileData findByUfno(Long ufno) {
        String sql = "select * from uploadfile where ufno = ? ";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(FileData.class), ufno);
    }

    /**
     * 파일 삭제
     * @param ufno 파일번호
     * @return
     */
    @Override
    public Integer delete(Long ufno) {
        String sql = "delete uploadFile where ufno = ? ";
        int resultCount = jdbcTemplate.update(sql, ufno);
        return resultCount;
    }

    /**
     * 파일 삭제 여러건
     * @param ufnos 파일번호
     * @return 결과
     */
    @Override
    public void delete(Long[] ufnos) {
        String sql = "delete uploadFile where ufno = ? ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, ufnos[i]);
            }

            @Override
            public int getBatchSize() {
                return ufnos.length;
            }
        });
    }
}
