package com.kh.heallo.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private Integer pageNo;         //페이지번호
    private Integer numOfRow;       //한 페이지 수
}
