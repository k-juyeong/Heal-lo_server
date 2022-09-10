package com.kh.heallo.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long rvno;                          //리뷰번호
    private double memno;                       //회원번호
    private String rvcontents;                  //리뷰컨텐츠
    private double rvscore;                     //리뷰별점
    private String rvcdate;                     //리뷰등록날짜
    private String memninkname;                 //회원닉네임
    private List<ReviewFileData> imageFiles;    //이미지파일들
    private boolean isLogin;                    //본인여부 확인

    //포멧변경
    public void setRvcdate(LocalDateTime rvcdate) {
        String format = rvcdate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.rvcdate = format;
    }
}
