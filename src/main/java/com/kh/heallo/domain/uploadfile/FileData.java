package com.kh.heallo.domain.uploadfile;

import lombok.Data;

@Data
public class FileData {
    private Long ufno;              //파일저장번호
    private Long bdno;              //게시글번호
    private Long cdno;              //캘린더번호
    private Long rvno;              //리뷰번호
    private String ufsname;         //로컬저장 파일명
    private String uffname;         //업로드 파일명
    private String uftype;          //파일유형
    private String ufpath;          //저장위치
    private Long ufsize;            //파일크기
}
