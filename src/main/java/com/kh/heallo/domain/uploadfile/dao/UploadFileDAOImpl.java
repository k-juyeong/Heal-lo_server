package com.kh.heallo.domain.uploadfile.dao;

import com.kh.heallo.domain.uploadfile.FileData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UploadFileDAOImpl implements UploadFileDAO {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 파일 업로드 (리뷰)
     * @param rvno 리뷰번호
     * @param fileData 파일정보
     * @return 결과 수
     */
    @Override
    public Integer ReviewFileUpload(Long rvno, FileData fileData) {
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into uploadfile ");
        sql.append(" values(uploadfile_ufno_seq.nextval,null,null,?, ");
        sql.append("         ?,?,?,?,?,");
        sql.append("         systimestamp,systimestamp) ");

        Integer resultCount = jdbcTemplate.update(sql.toString(),
                rvno,
                fileData.getUfsname(),
                fileData.getUffname(),
                fileData.getUfsize(),
                fileData.getUftype(),
                fileData.getUfpath()
        );

        return resultCount;
    }

    /**
     * 업로드 이미지 목록 조회 (리뷰)
     * @param rvno 리뷰번호
     * @return 이미지 리스트
     */
    @Override
    public List<FileData> findImagesByRvno(Long rvno) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * ");
        sql.append(" from uploadfile ");
        sql.append(" where rvno = ? ");

        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(FileData.class), rvno);
    }

    /**
     * 파일조회
     *
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
     *
     * @param ufno 파일번호
     * @return
     */
    @Override
    public Integer delete(Long ufno) {
        String sql = "delete uploadFile where ufno = ? ";
        int resultCount = jdbcTemplate.update(sql, ufno);
        return resultCount;
    }
}
