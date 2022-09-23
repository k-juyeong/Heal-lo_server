package com.kh.heallo.domain.uploadfile.dao;

import com.kh.heallo.domain.uploadfile.FileData;

import java.util.List;

public interface UploadFileDAO {

    /**
     * 파일 등록 단건
     * @param fileData 파일
     * @return 결과
     */
    Integer fileUpload(FileData fileData);

    /**
     * 파일 등록 - 여러건
     * @param fileDataList
     */
    void fileUpload(List<FileData> fileDataList);

    /**
     * 이미지파일 조회(복수)
     * @param code
     * @param noid
     * @return 이미지 리스트
     */
    List<FileData> findImages(String code, Long noid);

    /**
     * 파일조회
     * @param ufno
     * @return 파일
     */
    FileData findByUfno(Long ufno);

    /**
     * 리뷰에 새로 등록된 이미지 조회
     * @param fcno
     * @return
     */
    List<FileData> findNewReviewImage(Long fcno);

    /**
     * 파일 삭제
     * @param ufno 파일번호
     * @return 결과
     */
    Integer delete(Long ufno);

    /**
     * 파일 삭제 여러건
     * @param ufnos 파일번호
     * @return 결과
     */
    void delete(Long[] ufnos);
}
