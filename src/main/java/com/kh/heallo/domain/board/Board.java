package com.kh.heallo.domain.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {
  private Long bdno;
  private String bdcg;
  private String bdtitle;
  private Long memno;
  private String bdcontent;
  private Long ufno;
  private Long bdview;
  private Long bdhit;
  private LocalDateTime bdcdate;    //생성일
  private LocalDateTime bdudate;    //수정일
}
