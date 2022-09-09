package com.kh.heallo.domain.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCriteria {
    private Integer pageNo;         //페이지번호
    private Integer numOfRow;       //한 페이지 수
}
