package com.kh.heallo.web.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailForm {
  private String rpComment;        // RPCOMMENT	CLOB
  private LocalDateTime rpCDate;   // RPCDATE	TIMESTAMP(6)
  private LocalDateTime rpUDate;   // RPUDATE	TIMESTAMP(6)
}
