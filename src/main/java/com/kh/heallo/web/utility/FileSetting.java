package com.kh.heallo.web.utility;

import com.kh.heallo.domain.uploadfile.FileData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FileSetting {

    @Value("${file.path}")  //application.properties의 값 조회 (로컬주소는 임시)
    private String filePath;

    //로컬저장소에 파일저장
    public String transForTo(MultipartFile multipartFile) throws IOException {
        String localFileName = createLocalFileName(multipartFile);
        multipartFile.transferTo(new File(filePath + localFileName));

        return localFileName;
    }

    //GetFullPath
    public String getFullPath(String fileName) {

        return filePath + fileName;
    }

    //MultipartFile => FileData
    public FileData getFileData(MultipartFile multipartFile, String ufsname) {
            FileData fileData = new FileData();
            fileData.setUffname(multipartFile.getOriginalFilename());
            fileData.setUftype(multipartFile.getContentType());
            fileData.setUfsize(multipartFile.getSize());
            fileData.setUfsname(ufsname);
            fileData.setUfpath(filePath);

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
