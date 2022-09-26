package com.kh.heallo.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCriteriaDto {
    private Integer pageNo;         //페이지번호
    private String orderBy;   //정렬기준
}
