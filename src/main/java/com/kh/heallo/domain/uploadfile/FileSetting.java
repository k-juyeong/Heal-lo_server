package com.kh.heallo.domain.uploadfile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class FileSetting {

    @Value("${file.path}")  //application.properties의 값 조회 (로컬주소는 임시)
    private String basicFilePath;

    //로컬저장소에 파일저장
    public FileData transForTo(MultipartFile multipartFile, AttachCode code, Long noid) throws IOException {
        String localFileName = createLocalFileName(multipartFile);

        File file = new File(getFullPath(localFileName,code.getCode()));
        file.mkdirs();
        multipartFile.transferTo(file);

        FileData fileData = getFileData(multipartFile, code, noid, localFileName);

        return fileData;
    }

    public void deleteLocalFile(FileData fileData) {
        String fullPath = getFullPath(fileData.getUfsname(), fileData.getCode());
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
        } else {
            throw new IllegalArgumentException("첨부파일 삭제 실패: " + fileData.getCode() + " : " + fileData.getUfsname());
        }
    }

    //GetFullPath
    public String getFullPath(String fileName, String code) {

        return basicFilePath + code + "/" +  fileName;
    }

    //MultipartFile => FileData
    private FileData getFileData(MultipartFile multipartFile,AttachCode code, Long noid, String localFilename) {
            FileData fileData = new FileData();
            fileData.setCode(code.getCode());
            fileData.setNoid(noid);
            fileData.setUfsname(localFilename);
            fileData.setUffname(multipartFile.getOriginalFilename());
            fileData.setUftype(multipartFile.getContentType());
            fileData.setUfsize(multipartFile.getSize());
            fileData.setUfpath(basicFilePath);

        return fileData;
    }

    //로컬 저장소 파일이름 설정
    private String createLocalFileName(MultipartFile file) {
        String originName = file.getOriginalFilename();
        int extensionIndex = originName.lastIndexOf(".");
        String extension = originName.substring(extensionIndex+1, originName.length());
        String localFileName = UUID.randomUUID().toString() + "." + extension;

        return localFileName;
    }
}
