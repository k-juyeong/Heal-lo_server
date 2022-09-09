package com.kh.heallo.web.review.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewFileData {
    private String originFileName;  //업로드 파일명
    private String localFileName;   //로컬저장 파일명
}
