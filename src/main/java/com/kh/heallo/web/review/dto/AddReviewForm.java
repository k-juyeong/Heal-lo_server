package com.kh.heallo.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewForm {
    private Long rvno;                      //리뷰번호

    @NotBlank
    @Length(max = 1000)
    private String rvcontents;                  //리뷰컨텐츠

    @Max(50)
    private Integer rvline;               //컨텐츠 줄바꿈 횟수

    @NotNull
    @DecimalMax("5")
    @DecimalMin("0.5")
    private double rvscore;                     //리뷰별점
}
