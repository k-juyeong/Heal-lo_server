package com.kh.heallo.domain.file;

import lombok.Data;

@Data
public class FileData {
    private String originFileName;  //업로드 파일명
    private String localFileName;   //로컬저장 파일명
    private String localPath;       //저장위치
    private String fileType;        //파일유형
    private Long fileSize;          //파일크기
}
