package com.kh.heallo.domain.review;

import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.uploadfile.FileData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private Long rvno;                      //리뷰번호
    private String rvcontents;              //리뷰컨텐츠
    private double rvscore;                 //리뷰별점
    private LocalDateTime rvcdate;          //등록날짜
    private LocalDateTime rvudate;          //수정날짜
    private Long fcno;                    //운동시설번호
    private Long memno;                   //회원번호
    private List<FileData> imageFiles;   //등록이미지들
    private Member member;

}
