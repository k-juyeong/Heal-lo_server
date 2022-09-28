package com.kh.heallo.domain.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {
  private Long bdno;
  private Long no;
  private String bdcg;
  private String bdtitle;
  private Long memno;
  private String bdcontent;
  private Long ufno;
  private Long bdview;
  private Long bdhit;

  @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
  private LocalDateTime bdcdate;    //생성일

  @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
  private LocalDateTime bdudate;    //수정일

  private String memnickname; //닉네임

}
