package com.kh.heallo.domain.uploadfile.svc;

import com.kh.heallo.domain.uploadfile.AttachCode;
import com.kh.heallo.domain.uploadfile.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UploadFileSVC {

    /**
     * 파일 단일 등록
     * @param code
     * @param noid
     * @param file
     * @return
     */
    Integer fileUpload(AttachCode code, Long noid, MultipartFile file) throws IOException;

    /**
     * 파일 복수 등록
     * @param code
     * @param noid
     * @param files
     */
    void fileUpload(AttachCode code, Long noid, List<MultipartFile> files);

    /**
     * 파일 조회(복수)
     * @param code
     * @param noid
     * @return
     */
    List<FileData> findImages(AttachCode code, Long noid);

    /**
     * 파일 단일 삭제
     * @param ufno 파일번호
     * @return
     */
    Integer delete(Long ufno);

    /**
     * 파일 복수 삭제
     * @param ufnos 파일번호들
     * @return 결과
     */
    void delete(Long[] ufnos);
}
