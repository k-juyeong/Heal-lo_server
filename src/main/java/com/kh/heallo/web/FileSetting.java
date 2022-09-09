package com.kh.heallo.web;

import com.kh.heallo.domain.file.FileData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FileSetting {

    @Value("${file.path}")
    private String filePath;

    //로컬 저장소 파일이름 설정
    public String createLocalFileName(MultipartFile file) {
        String originName = file.getOriginalFilename();
        int extensionIndex = originName.lastIndexOf(".");
        String extension = originName.substring(extensionIndex, originName.length() - 1);
        String localFileName = UUID.randomUUID().toString() + "." + extension;

        return localFileName;
    }

    //
    public List<FileData> getFileDataList(List<MultipartFile> imageFiles) {
        List<FileData> fileDataList = imageFiles.stream().map(file -> {
            FileData fileData = new FileData();
            fileData.setOriginFileName(file.getOriginalFilename());
            fileData.setFileType(file.getContentType());
            fileData.setFileSize(file.getSize());
            fileData.setLocalFileName(createLocalFileName(file));
            fileData.setLocalPath(filePath);
            return fileData;
        }).collect(Collectors.toList());
        return fileDataList;
    }
}
