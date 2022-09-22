package com.kh.heallo.domain.bookmark;

import lombok.Data;

@Data
public class Bookmark {
    private Long bmno;      //즐겨찾기번호
    private Long memno;     //회원버호
    private Long fcno;      //운동시설번호
    private String status;  //즐겨찾기 on/off
}
