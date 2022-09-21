package com.kh.heallo.web.review.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewFileData {
    private Long ufno;
    private String code;
    private String Long;
    private String ufsname;         //로컬저장 파일명
    private String uffname;         //업로드 파일명
    private String uftype;          //파일유형
    private Long ufsize;            //파일크기
}
