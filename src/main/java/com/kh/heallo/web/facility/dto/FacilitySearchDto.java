package com.kh.heallo.web.facility.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilitySearchDto {
    private Long fcno;                  //운동시설번호    NUMBER
    private String fcname;	            //운동시설명		VARCHAR2
    private String fcimg;               //대표이미지		VARCHAR2
    private String fcaddr;              //주소			VARCHAR2
    private String fctel;               //전화번호		VARCHAR2
    private double fclat;	            //위도			NUMBER
    private double fclng;               //경도			NUMBER
    private double fcscore;             //평균평점	    NUMBER
    private Integer rvtotal;            //리뷰 전체 갯수
}
