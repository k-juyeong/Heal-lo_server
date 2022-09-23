package com.kh.heallo.domain.uploadfile.svc;

import com.kh.heallo.domain.uploadfile.AttachCode;
import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.domain.uploadfile.dao.UploadFileDAO;
import com.kh.heallo.domain.uploadfile.FileSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadFileSVCImpl implements UploadFileSVC{

    private final UploadFileDAO uploadFileDAO;
    private final FileSetting fileSetting;

    /**
     * 파일 단일 등록
     * @param code
     * @param noid
     * @param file
     * @return
     */
    @Override
    public Integer fileUpload(AttachCode code, Long noid, MultipartFile file) throws IOException {

        // 1) 로컬 저장소 파일 등록
        FileData fileData = fileSetting.transForTo(file, code, noid);

        // 2) 데이터베이스 파일 등록
        Integer resultCount = uploadFileDAO.fileUpload(fileData);

        return resultCount;
    }

    /**
     * 파일 복수 등록
     * @param code
     * @param noid
     * @param files
     */
    @Override
    public void fileUpload(AttachCode code, Long noid, List<MultipartFile> files){

        // 1) 로컬 저장소 파일 등록
        List<FileData> fileDataList = files.stream().map(file -> {
            FileData fileData;
            try {
                fileData = fileSetting.transForTo(file, code, noid);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return fileData;
        }).collect(Collectors.toList());

        // 2) 데이터베이스 파일 등록
        uploadFileDAO.fileUpload(fileDataList);
    }

    /**
     * 파일 조회(복수)
     * @param code
     * @param noid
     * @return
     */
    @Override
    public List<FileData> findImages(AttachCode code, Long noid) {

        // 1) FileDataList 조회
        List<FileData> fileDataList = uploadFileDAO.findImages(code.getCode(), noid);

        return fileDataList;
    }

    /**
     * 리뷰에 새로 등록된 이미지 조회
     *
     * @param fcno
     * @return
     */
    @Override
    public List<FileData> findNewReviewImage(Long fcno) {

        return uploadFileDAO.findNewReviewImage(fcno);
    }

    /**
     * 파일 단일 삭제
     * @param ufno 파일번호
     * @return
     */
    @Override
    public Integer delete(Long ufno) {
        FileData fileData = uploadFileDAO.findByUfno(ufno);

        // 데이터베이스 파일 삭제
        Integer resultCount = uploadFileDAO.delete(ufno);

        // 로컬 저장소 파일 삭제
        fileSetting.deleteLocalFile(fileData);

        return resultCount;
    }



    /**
     * 파일 복수 삭제
     * @param ufnos 파일번호들
     * @return 결과
     */
    @Override
    public void delete(Long[] ufnos) {

        // 데이터베이스 파일 삭제
        List<FileData> fileDataList = new ArrayList<>();
        for (Long ufno : ufnos) {
            FileData fileData = uploadFileDAO.findByUfno(ufno);
            fileDataList.add(fileData);
        }
        uploadFileDAO.delete(ufnos);

        // 로컬 저장소 파일 삭제
        fileDataList.stream().forEach(fileData -> {
            fileSetting.deleteLocalFile(fileData);
        });
    }
}
