package com.kh.heallo.domain.facility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Facility {
    private Long fcno;          //운동시설번호
    private String fcname;	    //운동시설명
    private String fctype;      //시설분류
    private String fchomepage;  //홈페이지
    private String fctel;       //전화번호
    private double fclat;	    //위도
    private double fclng;       //경도
    private String fcaddr;      //주소
    private String fcpostcode;  //우편번호
    private String fcstatus;    //운영상태
    private String fcimg;       //대표이미지
    private double fcscore;     //평균평점
    private Integer rvtotal;     //리뷰 전체 갯수

    public Facility(String fcname, String fctype, String fchomepage, String fctel,
                    double fclat, double fclng, String fcaddr, String fcpostCode,
                    String fcstatus, String fcimg, Integer rvtotal) {
        this.fcname = fcname;
        this.fctype = fctype;
        this.fchomepage = fchomepage;
        this.fctel = fctel;
        this.fclat = fclat;
        this.fclng = fclng;
        this.fcaddr = fcaddr;
        this.fcpostcode = fcpostCode;
        this.fcstatus = fcstatus;
        this.fcimg = fcimg;
        this.rvtotal = rvtotal;
    }
}
