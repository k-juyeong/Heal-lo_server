package com.kh.heallo.web.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddForm {
//  private Long rpGroup;       // RPGROUP	NUMBER(10,0)
//  private Long rpDepth;       // RPDEPTH	NUMBER(10,0)
  private String rpComment;   // RPCOMMENT	CLOB
}
