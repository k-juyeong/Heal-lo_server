package com.kh.heallo.domain.uploadfile.svc;

import com.kh.heallo.domain.uploadfile.FileData;

import java.util.List;

public interface UploadFileSVC {

    /**
     * 파일 업로드 (리뷰)
     * @param rvno 리뷰번호
     * @param fileData 파일정보
     * @return 결과 수
     */
    Integer ReviewFileUpload(Long rvno, FileData fileData);

    /**
     * 업로드 이미지 목록 조회 (리뷰)
     * @param rvno 리뷰번호
     * @return 이미지 리스트
     */
    List<FileData> findImagesByRvno(Long rvno);
}
