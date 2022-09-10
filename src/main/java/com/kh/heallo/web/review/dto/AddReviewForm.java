package com.kh.heallo.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewForm {
    private String rvcontents;                  //리뷰컨텐츠
    private double rvscore;                     //리뷰별점
    private List<MultipartFile> attachedImage;  //업로드이미지들
}
