package com.kh.heallo.domain.reply;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Reply {
  private Long rpno;               // RPNO	NUMBER(8,0)
  private Long bdno;               // BDNO	NUMBER(8,0)
  private Long memno;              // MEMNO	NUMBER(8,0)
  private String rpComment;        // RPCOMMENT	CLOB
  private LocalDateTime rpCDate;   // RPCDATE	TIMESTAMP(6)
  private LocalDateTime rpUDate;   // RPUDATE	TIMESTAMP(6)
}