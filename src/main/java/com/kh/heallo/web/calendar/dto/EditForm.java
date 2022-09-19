package com.kh.heallo.web.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Clob;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditForm {
  private Clob cdContent;  // CDCONTENT	CLOB	Yes		3
  private String cdRDate;    // CDRDATE	DATE	Yes		4
  private LocalDateTime cdCDate; // CDCDATE	DATE	Yes		5
}
