package com.kh.heallo.domain.facility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Facility {
    private Long fcno;          //운동시설번호    NUMBER
    private String fcname;	    //운동시설명		VARCHAR2
    private String fctype;      //시설분류		VARCHAR2
    private String fchomepage;  //홈페이지		VARCHAR2
    private String fctel;       //전화번호		VARCHAR2
    private double fclat;	    //위도			NUMBER
    private double fclng;       //경도			NUMBER
    private String fcaddr;      //주소			VARCHAR2
    private String fcpostcode;  //우편번호		NUMBER
    private String fcstatus;    //운영상태		VARCHAR2
    private String fcimg;       //대표이미지		VARCHAR2
    private double fcscore;     //평균평점	    NUMBER

    public Facility(String fcname, String fctype, String fchomepage, String fctel, double fclat, double fclng, String fcaddr, String fcpostCode, String fcstatus, String fcimg) {
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
    }
}
