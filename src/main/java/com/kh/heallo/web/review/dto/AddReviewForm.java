package com.kh.heallo.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewForm {
    private Long rvno;                      //리뷰번호

    @NotBlank
    @Length(max = 2000)
    private String rvcontents;                  //리뷰컨텐츠

    @NotNull
    @Range(min = 0,max = 5)
    private double rvscore;                     //리뷰별점

    @Size(max = 5)
    private List<ReviewFileData> imageFiles;    //업로드 이미지들
}
