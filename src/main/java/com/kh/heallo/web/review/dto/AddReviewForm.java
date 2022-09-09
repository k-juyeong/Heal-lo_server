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
    private String rvcontents;
    private double rvscore;
    private List<MultipartFile> attachedImage;
}
