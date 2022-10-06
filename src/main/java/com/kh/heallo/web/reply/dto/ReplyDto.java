package com.kh.heallo.web.reply.dto;

import com.kh.heallo.domain.board.Board;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ReplyDto {
  private Long rpno;               // RPNO	NUMBER(8,0)
  private Long bdno;               // BDNO	NUMBER(8,0)
  private Long memno;              // MEMNO	NUMBER(8,0)
  private String rpComment;        // RPCOMMENT	CLOB
  private LocalDateTime rpCDate;   // RPCDATE	TIMESTAMP(6)
  private Long rpGroup;            // RPGROUP	NUMBER(8,0)
  private Long rpDepth;            // RPDEPTH	NUMBER(10,0)
  private Long rpStep;             // RPSTEP	NUMBER(10,0)
  private String rpStatus;         // RPSTATUS	VARCHAR2(15 BYTE)

  @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
  private LocalDateTime rpUDate;   // RPUDATE	TIMESTAMP(6)
  private String memnickname; //닉네임
  private Board board;
  private boolean isLogin;

}
