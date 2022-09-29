package com.kh.heallo.web.board.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Data
public class DetailForm {
  private String bdcg;
  private Long bdno;
  private Long no;
  private String bdtitle;
  private String bdcontent;
  private Long memno;
  private Long bdview;

  @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
  private LocalDateTime bdcdate;  //작성일
  @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
  private LocalDateTime bdudate;  //수정일
  private String memnickname; //닉네임

}
