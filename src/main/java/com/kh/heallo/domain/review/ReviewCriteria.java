package com.kh.heallo.domain.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCriteria {
    private String orderBy;   //정렬기준
    private Integer pageNo;   //페이지번호
    private Integer startNo;
    private Integer endNo;
}
