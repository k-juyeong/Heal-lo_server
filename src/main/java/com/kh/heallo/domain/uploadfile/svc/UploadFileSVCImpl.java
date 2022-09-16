package com.kh.heallo.domain.uploadfile.svc;

import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.domain.uploadfile.dao.UploadFileDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadFileSVCImpl implements UploadFileSVC{

    private final UploadFileDAO uploadFileDAO;

    /**
     * 파일 업로드 (리뷰)
     *
     * @param rvno     리뷰번호
     * @param fileData 파일정보
     * @return 결과 수
     */
    @Override
    public Integer ReviewFileUpload(Long rvno, FileData fileData) {
        Integer resultCount = uploadFileDAO.ReviewFileUpload(rvno, fileData);
        return resultCount;
    }

    /**
     * 업로드 이미지 목록 조회 (리뷰)
     *
     * @param rvno 리뷰번호
     * @return 이미지 리스트
     */
    @Override
    public List<FileData> findImagesByRvno(Long rvno) {
        List<FileData> fileDataList = uploadFileDAO.findImagesByRvno(rvno);
        return fileDataList;
    }

    /**
     * 파일 삭제
     *
     * @param ufno 파일번호
     * @return
     */
    @Override
    public Integer delete(Long ufno) {
        FileData fileData = uploadFileDAO.findByUfno(ufno);
        String filePath = fileData.getUfpath() + fileData.getUfsname();
        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
        }
        return uploadFileDAO.delete(ufno);
    }
}
